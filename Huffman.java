/*
 * Name: Brian Yang
 * UNI: by2289
 */

import java.io.*;


public class Huffman
{
    public File myFile;
    public BufferedReader inputFile;
    public FileReader readFile;
    public Node root;
    public java.util.HashMap<Integer, Node> nodeHash;

    private static class lessThan implements java.util.Comparator<Node>
    {
        public int compare(Node lhs, Node rhs)
        {
            return Integer.compare(lhs.character, rhs.character);
        }
    }
    private static class Node implements Comparable<Node>
    {
        int character;
        Node left;
        Node right;
        int frequency;
        String huffmanCode;

        public Node()
        {
            character = -1;
            left = null;
            right = null;
            frequency = 0;
            huffmanCode = "";
        }

        public Node(int character, Node left, Node right, int frequency)
        {
            this.character = character;
            this.left = left;
            this.right = right;
            this.frequency = frequency;
        }

        public int compareTo(Node rhs)
        {
            return Integer.compare(this.frequency, rhs.frequency);
        }
    }
    public Huffman(String filename)
    {
        setFile(filename);
        nodeHash = computeFrequencies();
        root = buildTree();
        getCode(root, "");
    }
    public void setFile(String filename)
    {
        try
        {
            myFile = new File(filename);
            readFile = new FileReader(myFile);
            inputFile = new BufferedReader(readFile);
        }
        catch(FileNotFoundException e)
        {
            System.out.println("File " + filename + " not found...");
        }
    }
    public java.util.HashMap<Integer, Node> computeFrequencies()
    {
        java.util.HashMap<Integer, Node> frequencyHash = new java.util.HashMap<>();
        try
        {
            int character;
            while( (character = inputFile.read()) != -1)
            {
                //process each character
                //each character is an int value that is the ascii value for the char
                if(frequencyHash.containsKey(character))
                {
                    //if character was already read in before, update the frequency
                    frequencyHash.get(character).frequency += 1;
                }
                else
                {
                    //if character hasn't been read before, put it in the hash map
                    //with frequency 1
                    frequencyHash.put(character, new Node(character, null, null, 1));
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("IO Exception");
        }

        return frequencyHash;
    }
    public Node buildTree()
    {
        //traverse through the hashmap and create nodes for each character
        //and add them to a priority queue min heap

        java.util.PriorityQueue<Node> minHeap = new java.util.PriorityQueue<>();

        for (java.util.Map.Entry<Integer, Node> asciiVal : nodeHash.entrySet())
        {
            minHeap.add(asciiVal.getValue());
        }

        //process priority queue to build the tree
        //once minHeap's size is 1, we know that we have successfully built the tree
        //there is one node in the heap, which is the root of the tree.
        while(minHeap.size() > 1)
        {
            //get the two trees with lowest weight (lowest frequency)
            Node left = minHeap.remove();
            Node right = minHeap.remove();

            //merge these two trees, with the root of the tree having weight equal to the
            //sum of the weights of the two subtrees
            Node root = new Node(-1, left, right, left.frequency + right.frequency);

            //add the root back to the priority queue
            minHeap.add(root);
        }

        //tree has been built. Call remove on minHeap to get the root of the tree
        return minHeap.remove();
    }
    public void getCode(Node root, String code)
    {
        //perform a postorder tree traversal
        //when we reach a leaf node, the string s will have the huffman code
        if (root.left == null && root.right == null)
        {
            root.huffmanCode = code;
            nodeHash.get(root.character).huffmanCode = code;
            return;
        }

        // if we go left then add "0" to the code.
        // if we go right, then add "1" to the code.
        if(root.left != null)
        {
            getCode(root.left, code + "0");
        }
        if(root.right != null)
        {
            getCode(root.right, code + "1");
        }
    }
    public void printTable()
    {
        java.util.ArrayList<Node> tableArray = new java.util.ArrayList<>();
        getTableValues(root, tableArray);
        tableArray.sort(new lessThan());
        for(Node node : tableArray)
        {
            System.out.println((char)node.character + ": " + node.huffmanCode);
        }
    }
    private void getTableValues(Node root, java.util.ArrayList<Node> tableArray)
    {
        if(root.left == null && root.right == null)
        {
            tableArray.add(root);
        }
        if(root.left != null)
        {
            getTableValues(root.left, tableArray);
        }
        if(root.right != null)
        {
            getTableValues(root.right, tableArray);
        }
    }
    public String decode(String code)
    {
        //if there is an error, print error
        //keep reading each code character one by one, moving left if 0 is read, and
        //moving right if 1 is read
        //once we reach a leaf, print the character out
        StringBuilder decodedMessage = new StringBuilder();
        boolean valid = true;
        try
        {
            for(int i = 0; i < code.length(); i++)
            {
                if(code.charAt(i) != '1' && code.charAt(i) != '0')
                {
                    valid = false;
                    System.out.println("Code is invalid...Must contain only 0's and 1's..." +
                                       "Terminating decoding process...");
                    break;
                }
            }

            if(!valid)
            {
                throw new Exception("");
            }
            Node temp = root;
            for (int i = 0; i < code.length(); i++)
            {
                //0 is read, go left
                if (code.charAt(i) == '0')
                {
                    if(temp.left != null)
                    {
                        temp = temp.left;
                    }
                }
                //1 is read, go right
                if (code.charAt(i) == '1')
                {
                    if(temp.right != null)
                    {
                        temp = temp.right;
                    }
                }
                //if we reach the end of the code and we have not reached a leaf, there is error
                if(i == code.length() - 1 && (temp.left != null || temp.right != null))
                {
                    valid = false;
                    throw new Exception("Invalid code. Code length too short. Terminating " +
                                        "decoding process...");
                }

                //reach a leaf, print out the character
                if (temp.left == null && temp.right == null)
                {
                    decodedMessage.append((char) (temp.character));
                    //restart from the root to find the next character
                    temp = root;
                }
            }
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }

        if(!valid)
        {
            return "";
        }
        else
        {
            return "Decoded message: " + decodedMessage.toString();
        }
    }
    public String encode(String message)
    {
        System.out.println("Message: " + message);
        //read each character in the message, find the corresponding huffman code
        //for that character, and add it to the encoded message
        StringBuilder encodedMessage = new StringBuilder();
        boolean valid = true;
        try
        {
            for (int i = 0; i < message.length(); i++)
            {
                if (nodeHash.containsKey((int)message.charAt(i)))
                {
                    encodedMessage.append(nodeHash.get((int)message.charAt(i)).huffmanCode);
                }
                else
                {
                    valid = false;
                    System.out.println("Symbol \"" + message.charAt(i) + "\" is not in the " +
                                       "Huffman table. Unable to encode.");
                }
            }
            if(!valid)
            {
                throw new Exception("Terminating encoding process.");
            }
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        if(!valid)
        {
            return "";
        }
        else
        {
            return "Encoding of message: " + encodedMessage.toString();
        }
    }
    public static void main(String [] args)
    {
        try
        {
            /*
             The program should take as a command line argument the name of a file which
             contains some text. It should then compute the frequencies of the characters
             in that text and internally build the Huffman tree.  You should then print out
             in the console window a table of characters along with the corresponding
             Huffman codes.
             The program should then prompt the user to enter a code of 0's and ones.
             When you press enter the program should decode your input based on the
             Huffman tree that you constructed from the original input file. If there
             is an error in the code, print error, rather than the decoded message.

             Finally, the program should prompt the user for a series of characters.
             When the user presses enter, those characters should be converted into
             the corresponding Huffman code based on the Huffman tree built from the
             original file input.
             */

            
            if (args.length < 1)
            {
                throw new Exception("Too few arguments from command line.");
            }
            String filename = args[0];
            
            System.out.println("Creating Huffman Tree...");
            Huffman test = new Huffman(filename);
            System.out.println("Huffman Tree created...");
            System.out.println("Printing out Huffman Code table");
            test.printTable();

            java.util.Scanner keyboard = new java.util.Scanner(System.in);

            System.out.println("Please enter a code of 0's and 1's to get decoded message: ");
            String code = keyboard.next();
            System.out.println(test.decode(code));

            //read in newline character, so we can process the entire line
            //of the message for encoding
            code = keyboard.nextLine();
            System.out.print("Please enter a message with the letters in the table ");
            System.out.println("to get the encoding: " );
            String message = keyboard.nextLine();
            System.out.println(test.encode(message));
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
