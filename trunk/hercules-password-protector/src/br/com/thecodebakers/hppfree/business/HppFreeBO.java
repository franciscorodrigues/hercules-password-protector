package br.com.thecodebakers.hppfree.business;

import static br.com.thecodebakers.hppfree.constants.GlobalConstants.RETORNO_OK;

import java.io.File;
import java.io.StringReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.content.Context;
import android.database.SQLException;
import android.util.Log;
import android.widget.AutoCompleteTextView.Validator;
import br.com.thecodebakers.hppfree.constants.GlobalConstants;
import br.com.thecodebakers.hppfree.model.Agregador;
import br.com.thecodebakers.hppfree.model.Elemento;
import br.com.thecodebakers.hppfree.model.Message;
import br.com.thecodebakers.hppfree.persistence.DBAdapter;
import br.com.thecodebakers.hppfree.persistence.FileStorageAdapter;
import br.com.thecodebakers.hppfree.persistence.IDBAdapter;
import br.com.thecodebakers.hppfree.util.CriptoUtil;


/**
 * Esta classe é o principal Business Object do HPP. Ela fala com o Adapter do Banco e com as activities. 
 * Ela é um "Singleton" e deve ser recuperada com o método "getInstance()".
 * @author Cleuton Sampaio.
 *
 */
public class HppFreeBO implements IHppFreeBO {

	private static final String TAG = "HppFreeBO";
	private static HppFreeBO instance = null;
	private String key;
	private Context context;
	private IDBAdapter dbAdapter;
	private List<Elemento> lista;
	private static Message message = null;
	private static final String S = "\t\r\t";
	private static boolean vaultOpen = false;
	private int qtdeRestaurados = 0;
	
	
	public int getQtdeRestaurados() {
		return qtdeRestaurados;
	}

	public void setQtdeRestaurados(int qtdeRestaurados) {
		this.qtdeRestaurados = qtdeRestaurados;
	}

	public static boolean isVaultOpen() {
		return vaultOpen;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<Elemento> getLista() {
		return lista;
	}

	/**
	 * Recupera ou cria uma instância do Sigleton.
	 * @param context Contexto da aplicação Android.
	 * @return Instância do Singleton.
	 */
	public static HppFreeBO getInstance(Context context) {
		while (instance == null) {
			synchronized (HppFreeBO.class) {
				Log.d(TAG, "Criando uma nova instância do BO.");
				instance = new HppFreeBO(context);
			}
		}
		return instance;
	}
	
	/**
	 * Testa se existem elementos no banco de dados.
	 * @return true Se existirem elementos e false se o banco estiver vazio.
	 */
	public boolean isEmpty() {
		boolean resultado = false;
		List<Elemento> listaDB = dbAdapter.listAll();
		resultado = listaDB.isEmpty();
		listaDB = null;
		return resultado;
	}
	
	/**
	 * Construtor. Não deve ser invocado por outras classes.
	 */
	protected HppFreeBO(Context context) {
		dbAdapter = new DBAdapter(context);
		this.context = context;
		lista = new ArrayList<Elemento>();
	}

	public boolean openVault() {
		boolean retorno = false;
		List<Elemento> listaDB = null;
		Log.d(TAG, "Abrindo o cofre.");
		message = null;
		try {
			CriptoUtil.setUp(this.key);
			Log.d(TAG, "Recuperando a lista bruta.");
			listaDB = dbAdapter.listAll();
			DateFormat df = DateFormat.getDateTimeInstance();
			this.lista = new ArrayList<Elemento>();
			for (Elemento elemento : listaDB) {
				String clearText = CriptoUtil.decrypt(elemento.getLob()).toString();
				/*
				 * 0: local
				 * 1: Texto Secreto
				 * 2: Data última modificação Locale String
				 * 3: Hash armazenado
				 */
				String [] tokens = clearText.split(S); 
				elemento.setLocal(tokens[0]);
				elemento.setTextoSecreto(tokens[1]);
				elemento.setDataModificacao(
							df.parse(tokens[2])
						);
				elemento.setHashArmazenado(Integer.parseInt(tokens[3], 10));
				if (elemento.hashCode() == elemento.getHashArmazenado()) {
					Log.d(TAG, "Recuperou elemento: " + elemento.getUUID() + ".");
					this.getLista().add(elemento);
				}
				else {
					retorno = false;
					Log.e(TAG, "Hashcode não confere.");
					message = new Message(020, "Hashcode não confere.");
					break;
				}
			}
			if (message == null) {
				Collections.sort(this.lista);
				vaultOpen = true;
				Log.d(TAG, "Cofre aberto e lista criada.");
				retorno = true;
			}
			else {
				Log.d(TAG, "Cofre fechado porque a senha é inválida.");
			}
		}
		
		catch (SQLException sqe) {
			Log.e(TAG, "SQLException: " + sqe.getLocalizedMessage());
			message = new Message(030, "SQLException: " + sqe.getLocalizedMessage());
		}
		
		catch (Exception ex) {
			Log.e(TAG, "Exception: " + ex.getLocalizedMessage());
			message = new Message(030, "Exception: " + ex.getLocalizedMessage());
		}
		
		return retorno;
	}

	public boolean closeVault() {
		this.lista = null;
		this.key = null;
		message = null;
		vaultOpen = false;
		return true;
	}

	public boolean addElement(Elemento elemento) {
		boolean resultado = false;
		Log.d(TAG,"Entrou na adição de novo elemento.");
		message = null;
		elemento.setUUID(UUID.randomUUID().toString());
		elemento.setDataModificacao(new Date());
		elemento.setHashArmazenado(elemento.hashCode());
		try {
			dbAdapter.insert(elemento.getUUID(), getEncripted(elemento));
			Log.d(TAG, "Adicionou elemento: " + elemento.getUUID());
			lista.add(elemento);
			Collections.sort(this.lista);
			resultado = true;
		}
		catch (SQLException seq) {
			Log.e(TAG, "SQLException: " + seq.getLocalizedMessage());
			message = new Message(050, "SQLException: " + seq.getLocalizedMessage());
		} 
		catch (Exception e) {
			Log.e(TAG, "Exception: " + e.getLocalizedMessage());
			e.printStackTrace();
		}
		
		return resultado;
	}

	private byte [] getEncripted (Elemento elemento) throws Exception {
		Log.d(TAG, "Vai encriptar a entrada: " + elemento.getUUID());
		message = null;
		DateFormat df = DateFormat.getDateTimeInstance();
		String clearText = (  elemento.getLocal() 
							 + S
							 + elemento.getTextoSecreto()
							 + S
							 + df.format(elemento.getDataModificacao())
							 + S
							 + elemento.getHashArmazenado());
		CriptoUtil.setUp(this.key);
		byte [] cypherText = CriptoUtil.encrypt(clearText);
		return cypherText;
	}
	
	/**
	 * Re-encripta todos os elementos e atualiza a tabela.
	 * @return True se conseguiu
	 */
	public boolean reEncryptAll() {
		boolean resultado = true;
		Log.d(TAG, "Vai re-encriptar tudo,");
		for (Elemento el : this.lista) {
			resultado = saveElement(el);
			if (!resultado) {
				Log.e(TAG, "Erro ao salvar elemento.");
				resultado = false;
				break;
			}
		}
		return resultado;
	}
	
	/**
	 * Salva um elemento na lista e no SQLite
	 * @param Elemento - O elemento que se deseja salvar
	 * @return true se o Elemento foi salvo.
	 */
	public boolean saveElement(Elemento elemento) {
		boolean resultado = false;
		message = null;
		Log.d(TAG,"Entrou na alteração de elemento.");

		elemento.setDataModificacao(new Date());
		elemento.setHashArmazenado(elemento.hashCode());
		
		try {
			dbAdapter.update(elemento.getUUID(), getEncripted(elemento));
			Log.d(TAG, "Alterou elemento: " + elemento.getUUID());
			resultado = true;
		}
		catch (SQLException seq) {
			Log.e(TAG, "SQLException: " + seq.getLocalizedMessage());
			message = new Message(050, "SQLException: " + seq.getLocalizedMessage());
		} 
		catch (Exception e) {
			Log.e(TAG, "Exception: " + e.getLocalizedMessage());
			e.printStackTrace();
		}
		
		return resultado;
	}

	public boolean removeElement(Elemento elemento) {
		boolean resultado = false;
		Log.d(TAG,"Entrou na remoção de elemento.");
		message = null;
		try {
			dbAdapter.delete(elemento.getUUID());
			Log.d(TAG, "Removeu elemento: " + elemento.getUUID());
			lista.remove(elemento);
			resultado = true;
		}
		catch (SQLException seq) {
			Log.e(TAG, "SQLException: " + seq.getLocalizedMessage());
			message = new Message(050, "SQLException: " + seq.getLocalizedMessage());
		} 
		catch (Exception e) {
			Log.e(TAG, "Exception: " + e.getLocalizedMessage());
			e.printStackTrace();
		}
		
		return resultado;
	}

	public Message getLastMessage() {
		return message;
	}

	/**
	 * Armazena uma cópia dos dados do cofre dentro dos contatos. Utiliza o nome: "HPPFREEn" onde "n" é o número da versão do banco de dados.
	 * @return Código de resultado, de acordo com GlobalConstants.
	 */
	public int saveBackup() {
		Log.d(TAG, "Solicitou salvar backup.");
		int resultado = RETORNO_OK;
		String docXML = generateXML();
		
		if ((resultado = FileStorageAdapter.setXMLString(docXML, this.context)) != GlobalConstants.RETORNO_OK) {
			Log.e(TAG, "Erro ao salvar o XML nos contatos.");
		}
		else {
			Log.d(TAG, "XML salvo com sucesso.");
		}
		
		return resultado;
	}
	
	private String generateXML() {
		SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
		// YYYY-MM-DDThh:mm:ss
		sdf.applyPattern("yyyy-MM-DD'T'HH:mm:ss");
		String dataNow = sdf.format(new Date());
		StringBuffer plainList = new StringBuffer();
		StringBuffer xmlList = new StringBuffer();
		String docXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n";
		docXML += "<tns:hppfreeBackup \r\n" 
			+ " xmlns:tns=\"http://br.com.thecodebakers.hpp/hppfreebackup\" \r\n" 
			+ " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \r\n" 
			+ " xsi:schemaLocation=\" br/com/thecodebakers/hppfree/schema/hppfreebackup.xsd \">\r\n";
		docXML += "\t<tns:versaoData>" + dbAdapter.getDatabaseVersion() + "</tns:versaoData>\r\n";
		docXML += "\t<tns:data>" + dataNow +  "</tns:data>\r\n";
		
		List<Agregador> lAgreg = dbAdapter.listSource();
		xmlList.append("\t<tns:entradas>\r\n");
		for (Agregador a : lAgreg) {
			plainList.append(a.UUID + a.textoSecreto);
			xmlList.append("\t\t<tns:entrada>\r\n" + 
						   "\t\t\t<tns:texto1>" + a.UUID + "</tns:texto1>\r\n" + 
						   "\t\t\t<tns:texto2>" + a.textoSecreto + "</tns:texto2>\r\n" + 
						   "\t\t</tns:entrada>\r\n"
					);
		}
		xmlList.append("\t</tns:entradas>\r\n");
		
	    MessageDigest md = null;  
	    try {  
	        md = MessageDigest.getInstance("MD5");
	        
	    } catch (NoSuchAlgorithmException e) {  
	        e.printStackTrace();  
	    }  

	    String hashPlain = Integer.toString(new BigInteger(md.digest(plainList.toString().getBytes())).intValue());
		docXML += "\t<tns:hashTexto>" + encriptar(hashPlain, this.key) + "</tns:hashTexto>\t\n";
		docXML += xmlList.toString();
		docXML += "</tns:hppfreeBackup>\r\n";
		return docXML;
	}

	private String encriptar(String hashPlain, String key2) {
		StringBuffer saida = new StringBuffer();
		try {
			CriptoUtil.setUp(key2);
			byte [] hashcript = CriptoUtil.encrypt(hashPlain);
			
			for (int x = 0; x < hashcript.length; x++) {
				saida.append(String.format("%02X", hashcript[x]));
			}		
		} catch (Exception e) {
			Log.e(TAG, "Exception ao encriptar o hashcode: " + e.getLocalizedMessage());
		}

		return saida.toString();
	}

	/**
	 * Restaura um backup. Apaga todos os registros da tabela e armazena os que foram salvos. Testa o hashcode antes.
	 * @return Código de resultado, de acordo com GlobalConstants.
	 */
	public int restoreBackup(String chave) {
		
		int retorno = GlobalConstants.ARQUIVO_BACKUP_INVALIDO;
		String xml = FileStorageAdapter.getXMLString(context);
		int hashCodeTexto = 0;
		
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        List<Message> messages = new ArrayList<Message>();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document dom = builder.parse(new InputSource(new StringReader(xml)));
            Element root = dom.getDocumentElement();
            NodeList items = root.getElementsByTagName("tns:versaoData");
            if (items.getLength() == 0) {
            	Log.e(TAG, "Faltou o elemento <tns:versaoData />.");
            	retorno = GlobalConstants.ARQUIVO_BACKUP_INVALIDO;
            }
            else {
            	// Ok, tem a versão do backup. Vamos ver se é compatível (<= atual)
            	Element versaoData = (Element) items.item(0);
            	int versao = Integer.parseInt(versaoData.getFirstChild().getNodeValue());
            	if (versao > dbAdapter.getDatabaseVersion()) {
            		retorno = GlobalConstants.ERRO_VERSAO_BACKUP_INCOMPATIVEL;
            	}
            	else {
            		// OK, é compatível, vamos ver se existe o hashcode: <tns:hashTexto />
            		items = root.getElementsByTagName("tns:hashTexto");
                    if (items.getLength() == 0) {
                    	Log.e(TAG, "Faltou o elemento <tns:hashTexto />.");
                    	retorno = GlobalConstants.ARQUIVO_BACKUP_INVALIDO;
                    }
                    else {
                    	// OK, existe o tag hashTexto. Vamos guardar para verificar mais tarde...
                    	Element hashTexto = (Element) items.item(0);
                    	String hashString = hashTexto.getFirstChild().getNodeValue();
                    	hashCodeTexto = decriptar(hashString, chave);
                    	// Vamos ler as entradas todas <tns:entradas />
                    	items = root.getElementsByTagName("tns:entradas");
                    	if (items.getLength() == 0) {
                    		Log.e(TAG, "Não achou o tag <tns:entradas />.");
                    		retorno = GlobalConstants.ARQUIVO_BACKUP_INVALIDO;
                    	}
                    	else {
                    		// Achou o tag <tns:entradas /> Vai processar cada <tns:entrada />
                    		Log.d(TAG, "Até agora, backup ok. Vai processar as entradas.");
                    		Element entradas = (Element) items.item(0);
                    		items = entradas.getElementsByTagName("tns:entrada");
                    		// loop de validação das entradas. Depois tem que validar o hashcode.... ***************************\
                    		Element entrada = null;
                    		Element texto1 = null;
                    		Element texto2 = null;
                    		Elemento nElemento = null;
                    		StringBuffer plainList = new StringBuffer();
                    		int contador = 0;
                    		List<Elemento> novaLista = new ArrayList<Elemento>();
                    		for (int x = 0; x < items.getLength(); x++) {
                    			entrada = (Element) items.item(x);
                    			texto1 = (Element) entrada.getElementsByTagName("tns:texto1").item(0);
                    			if (texto1 == null) {
                    				retorno = GlobalConstants.ARQUIVO_BACKUP_INVALIDO;
                    				throw new Exception("Falta o elemento <tns:texto1/>");
                    			}
                    			texto2 = (Element) entrada.getElementsByTagName("tns:texto2").item(0);
                    			if (texto2 == null) {
                    				retorno = GlobalConstants.ARQUIVO_BACKUP_INVALIDO;
                    				throw new Exception("Falta o elemento <tns:texto2/>");
                    			}
                    			String stexto1 = texto1.getFirstChild().getNodeValue();
                    			String stexto2 = texto2.getFirstChild().getNodeValue();
                    			plainList.append(stexto1);
                    			plainList.append(stexto2);
                    			nElemento = new Elemento();
                    			nElemento.setUUID(stexto1);
                    			nElemento.setLob(toByteAgain(stexto2));
                    			novaLista.add(nElemento);
                    			contador++;
                    			Log.d(TAG, "Recuperou elemento número: " + contador + ", uuid: " + nElemento.getUUID());
                    		}
                    		
                    		// Acabou de ler a lista. Agora vamos verificar o hash do arquivo e o hash de cada entrada.
                    		Log.d(TAG, "Foram recuperados do arquivo: " + contador + " elementos.");
                    		this.qtdeRestaurados = contador;
                    	    MessageDigest md = null;  
                    	    try {  
                    	        md = MessageDigest.getInstance("MD5");
                        	    int hashCalc = new BigInteger(md.digest(plainList.toString().getBytes())).intValue();                    		
                        		if (hashCalc != hashCodeTexto) {
                        			retorno = GlobalConstants.HASHCODE_BACKUP_INVALIDO;
                        			Log.d(TAG, "Hashcode do arquivo de backup inválido.");
                        			throw new Exception("HashCode inválido");
                        		}
                        		
                        		// Ok. o hashcode confere. Agora, vamos decriptar e testar a nova lista
                        		Log.d(TAG, "O Hashcode do arquivo está correto.");
                        		plainList = null;
                        		DateFormat df = DateFormat.getDateTimeInstance();
                        		for (Elemento e : novaLista) {
                        			CriptoUtil.setUp(chave);
                    				String clearText = CriptoUtil.decrypt(e.getLob()).toString();
                    				/*
                    				 * 0: local
                    				 * 1: Texto Secreto
                    				 * 2: Data última modificação Locale String
                    				 * 3: Hash armazenado
                    				 */
                    				String [] tokens = clearText.split(S); 
                    				e.setLocal(tokens[0]);
                    				e.setTextoSecreto(tokens[1]);
                    				e.setDataModificacao(
                    							df.parse(tokens[2])
                    						);
                    				e.setHashArmazenado(Integer.parseInt(tokens[3], 10));
                    				if (e.hashCode() == e.getHashArmazenado()) {
                    					Log.d(TAG, "Recuperou elemento do backup: " + e.getUUID() + ".");
                    				}
                    				else {
                    					retorno = GlobalConstants.ERRO_HASHCODE_ENTRADA_INVALIDO;
                    					Log.e(TAG, "Hashcode não confere em uma das entradas.");
                    					throw new Exception("Hashcode não confere em uma das entradas.");
                    				}
                        			
                        		}
                        		
                        		// Bem, temos uma nova lista. Agora vamos chamar um método do adapter que vai iniciar uma transação.
                        		Log.d(TAG, "Todas as entradas estão corretas.");
                        		// Se tudo estiver ok, marca o retorno do método: 
                        		if (dbAdapter.updateNewList(novaLista, chave)) {
                            		retorno = GlobalConstants.RETORNO_OK;
                        		}
                        		else {
                        			retorno = GlobalConstants.ERRO_RESTAURAR_BACKUP_NO_BANCO;
                        			Log.e(TAG, "Erro ao restaurar backup no banco.");
                        		}
                        		
                    	    } catch (NoSuchAlgorithmException e) {
                    	    	retorno = GlobalConstants.ARQUIVO_BACKUP_INVALIDO;
                    	        Log.e(TAG, "Exception ao tentar obter o algoritmo MD5 para verificar o hash");  
                    	    }  

                    	}
                    }
            	}
            }
 
        } 
        catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getLocalizedMessage());
        } 
 
		
		return retorno;
	}

	private byte[] toByteAgain(String stexto2) {
		int pos = 0;
		byte [] blob = new byte[stexto2.length()/2];
		for (int x = 0; x < stexto2.length(); x = x + 2) {
			String hex1 = stexto2.substring(x, x+2);
			blob[pos] = (byte) Integer.parseInt(hex1, 16);
			pos++;			
		}		
		return blob;
	}

	private int decriptar(String hashString, String chave) {
		int retorno = 0;
		int pos = 0;
		byte [] blob = new byte[hashString.length()/2];
		try {
			for (int x = 0; x < hashString.length(); x = x + 2) {
				String hex1 = hashString.substring(x, x+2);
				blob[pos] = (byte) Integer.parseInt(hex1, 16);
				pos++;
			}
		
			CriptoUtil.setUp(chave);
			String sretorno = CriptoUtil.decrypt(blob);
			retorno = Integer.parseInt(sretorno);
		} catch (Exception e) {
			Log.e(TAG, "Exception ao decriptar o hash: " + e.getLocalizedMessage());
		}
		
		return retorno;
	}

	private String decriptarString(String hashString, String chave) {
		String sretorno = "";
		int pos = 0;
		byte [] blob = new byte[hashString.length()/2];
		try {
			for (int x = 0; x < hashString.length(); x = x + 2) {
				String hex1 = hashString.substring(x, x+2);
				blob[pos] = (byte) Integer.parseInt(hex1, 16);
				pos++;
			}
		
			CriptoUtil.setUp(chave);
			sretorno = CriptoUtil.decrypt(blob);
		} catch (Exception e) {
			Log.e(TAG, "Exception ao decriptar texto1 ou texto2: " + e.getLocalizedMessage());
		}
		
		return sretorno;
	}
	
	
}
