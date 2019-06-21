import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

// ATENCAO: ANTES DE EXECUTAR ESTE CODIGO, LEIA AS INSTRUCOES DE USO
// NO README.md PARA ENTENDER SUA FUNCIONALIDADE

public class Main {
    
    // Este valor serve para auxiliar nas equacoes para geracao das chaves
    // Este ONE serve como o valor na conta dos expoentes, representado em BigInteger (ja que nao podemos usar int = 1)
    private final static BigInteger ONE = new BigInteger("1");
    private final static int ITERATION_LIMIT = 12000;
    private static Random rand = new Random();
    
    public static void main(String[] args){
        
        Scanner in = new Scanner(System.in);
        
        // DIGITE 1024 OU MAIOR
        // Motivo: Existe uma condicao que impede a entrada de valores de 1000 ou menos bits
        // *Requisito do enunciado*
        System.out.print("Digite o numero de bits (recomendado 1024 ou maior): ");
        int BITS = Integer.parseInt(in.nextLine());
        System.out.println();
        
        /*-----------------------------------------------------------*/
        
        // PARTE 1: Geracao dos numeros aleatorios
        System.out.print("Gerando numeros primos aleatorios...\nIsto pode levar um tempinho!\n\n");
        BigInteger p = generatePrime(BITS,"p");
        BigInteger q = generatePrime(BITS,"q");
        System.out.println();
        System.out.println("Numero p: " + p);
        System.out.println("================");
        System.out.println("Numero q: " + q);
        
        /*-----------------------------------------------------------*/
        
        // PARTE 2: Geracao das chaves
        
        // Calculo do modulo = p*q
        BigInteger mod = p.multiply(q);
        
        // Calculo do phi = (p-1)*(q-1)
        BigInteger phi = (p.subtract(ONE)).multiply(q.subtract(ONE));
        
        // Calcula-se o "e" para gerar a chave publica, onde as condicoes sao:
        // 1 < e < phi(mod) && gcd(e, phi(mod)) = 1
        BigInteger e = new BigInteger(BITS, rand);
        System.out.println("\n\nGerando chave publica...\n");
        // Precisou usar compareTo(), pois ">" e "<" nao funcionavam
        // compareTo() --> 1 valor maior, 0 igual, -1 valor menor
        // Usou-se um metodo pronto do BigInteger para o GCD
        // Escolhe-se um numero aleatorio "e" e aplica o maior divisor comum nele
        // https://www.geeksforgeeks.org/biginteger-compareto-method-in-java/
        // https://www.tutorialspoint.com/java/math/biginteger_gcd.htm
        //
        // while --> 1 < e < phi(mod) && gcd(e, phi(mod)) = 1
        while(e.compareTo(ONE) <= 0 || e.compareTo(phi) >= 0 || !e.gcd(phi).equals(ONE)){
            e = new BigInteger(BITS, rand);
        }
        System.out.println("Chave publica em DEC: " + e);
        System.out.println("================");
        byte[] e_byte = e.toByteArray();
        String e_hex = byteArrayToHexString(e_byte);
        System.out.println("Chave publica em HEX: " + e_hex);
        
        // Para a geracao da chave privada, que envolve calculo inverso
        // usou um metodo pronto chamado modInverse()
        System.out.println("\n\nGerando chave privada...\n");
        BigInteger d = e.modInverse(phi);
        System.out.println("Chave privada em DEC: " + d);
        System.out.println("================");
        byte[] d_byte = d.toByteArray();
        String d_hex = byteArrayToHexString(d_byte);
        System.out.println("Chave privada em HEX: " + d_hex);
        
        /*-----------------------------------------------------------*/
        
        // PARTE 3: Cifragem e decifragem da mensagem
        
        System.out.println("\n\n");
        System.out.print("Digite a mensagem: ");
        String mensagem = in.nextLine();
        System.out.println();
        System.out.println("Mensagem de entrada: " + mensagem);
        
        //Cifrando a mensagem (converte String pra bytes, depois para BigInteger e cifra usando modPow)
        byte[] msg_to_bytes = mensagem.getBytes();
        // Imprime a m
        System.out.print("Mensagem cifrada em bytes: ");
        for(byte b:msg_to_bytes)
            System.out.print(b);
        System.out.println();
        BigInteger c_msg = new BigInteger(msg_to_bytes);
        System.out.println("Mensagem cifrada em BigInteger: " + c_msg);
        // Aqui, computa C = msg^e % mod, onde 0 ≤ msg < mod
        BigInteger c_enc = c_msg.modPow(e, mod);
        System.out.println("Mensagem cifrada: " + c_enc);
        
        //Decifrando a mensagem (decifra usando modPow, converte BigInteger para bytes e depois para String)
        System.out.println();
        // Aqui computa  msg = C^d % mod
        BigInteger d_msg = c_enc.modPow(d, mod);
        System.out.println("Decifrando a mensagem com a chave privada...");
        byte[] dec_to_bytes = d_msg.toByteArray();
        String mensagem_decifrada = new String(dec_to_bytes);
        System.out.println("Mensagem decifrada: " + mensagem_decifrada);
        
        in.close();
    }
    
    // Aqui fica a logica de Fermat, onde um numero primo aleatorio sera gerado, onde
    // a entrada do usuario dos BITS sera usada.
    // Ele gera qualquer numero aleatorio dentro de [2^BITS] ate [2^BITS-1] 
    // Depois ele verifica se este numero gerado e primo, caso contrario, gera outro.
    // A verificacao sera feita no metodo fermatPrime() abaixo
    // Ela tambem garante que o numero gerado tenha pelo menos mais de 1000 bits.
    private static BigInteger generatePrime(int BITS, String id){
        int iterations = 0;
        BigInteger randomNumber = new BigInteger(BITS,rand);
        while (!fermatPrime(randomNumber) || randomNumber.bitLength() <= 1000) {
            iterations++;
            randomNumber = new BigInteger(BITS,rand);
            //System.out.println("Bit Length para " + id + ": " + randomNumber.bitLength());
            
            // Se ele exceder o limite de iteracoes, da timeout
            // Cuidar entao com numeros excessivamente grandes (tipo bits maiores que 5000)
            if(iterations > ITERATION_LIMIT){
                System.out.println("Limite de iteracoes excedido!");
                System.out.println("Timeout no sistema!");
                System.exit(1);
            }
        }
        System.out.println("Numero de iteracoes para gerar numero aleatorio para " + id + ": " + iterations);
        return randomNumber;
    }
    
    private static boolean fermatPrime(BigInteger num) {
        BigInteger a = new BigInteger("2");
        a = a.modPow(num.subtract(ONE), num);
        if (a.compareTo(ONE) == 0) {
            return true;
        }
        return false;
    }
    
    // Metodo de conversao usado do Moodle
    private static String byteArrayToHexString(byte[] b)
    {
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
    }
    
}
