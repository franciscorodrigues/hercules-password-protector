package br.com.thecodebakers.hppfree.business;

import java.util.List;

import br.com.thecodebakers.hppfree.model.Elemento;
import br.com.thecodebakers.hppfree.model.Message;
/**
 * Interface para a classe BO que gerencia o "cofre" de dados.
 * @author Cleuton Sampaio
 *
 */
public interface IHppFreeBO {
	/**
	 * Abre o "cofre", carregando a lista interna.
	 * @return true sucesso, false problema. Use "getLastMessage()" para saber o problema.
	 */
	public boolean openVault();
	/**
	 * Fecha o "cofre", destruindo a lista interna;
	 * @return true sucesso, false problema. Use "getLastMessage()" para saber o problema.
	 */
	public boolean closeVault();
	/**
	 * Adiciona um novo elemento à lista e ao banco de dados simultaneamente.
	 * @param elemento Conjunto de dados a ser adicionado.
	 * @return true sucesso, false problema. Use "getLastMessage()" para saber o problema.
	 */
	public boolean addElement(Elemento elemento);
	/**
	 * Atualiza um elemento na lista e ao banco de dados simultaneamente.
	 * @param elemento Conjunto de dados a ser atualizado.
	 * @return true sucesso, false problema. Use "getLastMessage()" para saber o problema.
	 */
	public boolean saveElement(Elemento elemento);
	/**
	 * Remove um elemento da lista e do banco de dados simultaneamente.
	 * @param elemento Conjunto de dados a ser removido.
	 * @return true sucesso, false problema. Use "getLastMessage()" para saber o problema.
	 */	
	public boolean removeElement(Elemento elemento);
	/**
	 * Retorna a última mensagem gerada pelo BO. 
	 * @return instância de br.com.thecodebakers.hppfree.model.Message ou null.
	 */
	public Message getLastMessage();

}
