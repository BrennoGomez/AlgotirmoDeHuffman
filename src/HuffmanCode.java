
import java.util.Scanner;

/**
 * @author Breno Gomes
 */
public class HuffmanCode {

    public static void main(String[] args) {
        HuffmanCode huffmanCode = new HuffmanCode();
        String text = huffmanCode.getText();
        HuffmanTree huffmanTree = new HuffmanTree();
        String cryptedText = huffmanTree.crypt(text);							//realiza a compress�o
        System.out.println("Crypted: " + cryptedText);
        System.out.println("Decrypted: " + huffmanTree.decrypt(cryptedText));	//realiza a descompress�o
    }

    /**
     * Realiza a leitura do texto digitado no console.
     */
    private String getText() {
        Scanner io = new Scanner(System.in);
        System.out.println("Enter the string: ");
        return io.nextLine();
    }    
}
