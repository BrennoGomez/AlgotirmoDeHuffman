
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Breno Gomes
 */
public class HuffmanTree {

    private HuffmanNode root; //se comporta como uma cifra

    /**
     * Analisa a frequ�ncia de cada caractere e monta um map, onde a chave é o caractere e a frequ�ncia é o valor.
     */
    private Map<Character, Integer> getCharFrequency(String nonCryptedText) {
        char[] arrayOfChars = nonCryptedText.toCharArray();
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char letter : arrayOfChars) {
            if (frequencyMap.get(letter) == null) {
                frequencyMap.put(letter, 1);
            } else {
                frequencyMap.put(letter, frequencyMap.get(letter) + 1);
            }
        }
        return frequencyMap;
    }

    /**
     * Cria um mapa para os valores bin�rios, onde a chave � o caractere e a string bin�ria � o valor.
     */
    private Map<Character, String> createBinaryMap(Map<Character, Integer> frequencyMap) {
        Map<Character, String> binaryMap = new HashMap<>(frequencyMap.size());
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            binaryMap.put(entry.getKey(), null);
        }
        return binaryMap;
    }

    /**
     * Ordena as ra�zes na an�lise de Huffman.
     */
    private List<HuffmanNode> sortRoots(List<HuffmanNode> roots) {
        Collections.sort(roots, new Comparator<HuffmanNode>() {
            @Override
            public int compare(HuffmanNode o1, HuffmanNode o2) {
                return o1.getFrequency().compareTo(o2.getFrequency());
            }
        });
        return roots;
    }

    /**
     * Inicializa a �rvore de Huffman. Monta a lista de ra�zes.
     */
    private List<HuffmanNode> initializeTree(Map<Character, Integer> frequencyMap) {
        List<HuffmanNode> roots = new ArrayList<>();
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            HuffmanNode node = new HuffmanNode();
            node.setContent(entry.getKey());
            node.setFrequency(entry.getValue());
            //Marco inicial todos como folha
            node.setIsLeaf(true);
            roots.add(node);
        }
        return sortRoots(roots);
    }
    
    /**
     * Cria a �rvore de Huffman.
     */
    private HuffmanNode createTree(Map<Character, Integer> charFrequencyMap) {
        List<HuffmanNode> priorityRootList = this.initializeTree(charFrequencyMap);

        while (priorityRootList.size() > 1) {
            //pega o primeiro e o segundo n� da lista de ra�zes
            HuffmanNode firstNode = priorityRootList.get(0);
            HuffmanNode secondNode = priorityRootList.get(1);
            //cria��o do novo n�
            HuffmanNode newRoot = new HuffmanNode();
            //Associa os n�s como filhos
            newRoot.setLeft(firstNode);
            newRoot.setRight(secondNode);
            //soma as frequ�ncias dos filhos
            newRoot.setFrequency(newRoot.getLeft().getFrequency() + newRoot.getRight().getFrequency());
            //marca o novo n� s� para fins de visualiza��o
            newRoot.setContent(666);
            //adiciona na lista
            priorityRootList.add(newRoot);
            //remove os n�s antigos
            priorityRootList.remove(firstNode);
            priorityRootList.remove(secondNode);
            //reordena a lista de n�s
            this.sortRoots(priorityRootList);
        }
        return priorityRootList.get(0);
    }

    /**
     * Este m�todo realiza a compress�o de todo o texto e utiliza sua vers�o sobrecarregada para consultar a �rvore. O caractere tra�o (-) � usado 
     * apenas para a visualiza��o da sequencia bin�ria.
     * @param nonCryptedText Representa o texto n�o comprimido.
     * @return String Retorna o texto comprimido.
     */
    public String crypt(String nonCryptedText) {
        char[] arrayCryptedText = nonCryptedText.toCharArray();
        final char SPACE = '-';
        StringBuilder accumulatorCryptedText = new StringBuilder();
        //Cria��o da tabela de frequ�ncia.
        Map<Character, Integer> charFrequencyMap = this.getCharFrequency(nonCryptedText);
        //Cria��o da tabela bin�ria, relaciona o caractere � sequencia bin�ria 
        Map<Character, String> binaryMap = this.createBinaryMap(charFrequencyMap);
        //Cria a �rvore de Huffman
        this.root = this.createTree(charFrequencyMap);
        //Chamada ao m�todo recursivo
        this.crypt(this.root, "", binaryMap);

        for (int i = 0; i < arrayCryptedText.length; i++) {
            char nonCrypedLetter = arrayCryptedText[i];
            accumulatorCryptedText.append(binaryMap.get(nonCrypedLetter));
            if (i != arrayCryptedText.length - 1) {
                accumulatorCryptedText.append(SPACE);
            }
        }

        return accumulatorCryptedText.toString();
    }

    /**
     * Este m�todo de fato realiza a compress�o dos dados atrav�s de buscas recursivas na �rvore de Huffman.
     * @param root Ra�z da �rvore. � necess�rio come�ar deste ponto a busca.
     * @param currentBinary Representa a atual sequencia bin�ria sendo montada.
     * @param binaryMap Este mapa � utilizado como um facilitador para o armazenamento das sequencias bin�rias. Ele age como a tabela de Huffman.
     */
    private void crypt(HuffmanNode root, String currentBinary, Map<Character, String> binaryMap) {
        //se � folha � porque cheguei ao caractere
        if (root.isLeaf()) {
            binaryMap.put((char) root.getContent(), currentBinary);
        } else {
            //cada vez que des�o � esquerda concateno 0 na sequencia bin�ria.
            crypt(root.getLeft(), currentBinary.concat("0"), binaryMap);
            //cada vez que des�o � direita concateno 1 na sequencia bin�ria.
            crypt(root.getRight(), currentBinary.concat("1"), binaryMap);
        }
    }

    /**
     * Realiza a descompress�o.
     * @param cryptedText Representa o texto comprimido.
     * @return String Retorna o texto descomprimido.
     */
    public String decrypt(String cryptedText) {
        String[] arrayCryptedWords = cryptedText.split("-");
        StringBuilder accumulatorDecryptedText = new StringBuilder();
        for (String cryptedWord : arrayCryptedWords) {
            HuffmanNode currentNode = this.root;
            for (char cryptedLetter : cryptedWord.toCharArray()) {
                //para cada 0 vai para esquerda e para cada 1 vai para a direita
                if (cryptedLetter == '0') {
                    currentNode = currentNode.getLeft();
                } else {
                    currentNode = currentNode.getRight();
                }
                //se for folha � porque chegou no caractere
                if (currentNode.isLeaf()) {
                    accumulatorDecryptedText.append((char) currentNode.getContent());
                }
            }
        }
        return accumulatorDecryptedText.toString();
    }
}
