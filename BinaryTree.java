public class BinaryTree { // class used for each input
    String sentence;
    Node root;
    double result;

    public BinaryTree(String sentence) {
        this.sentence = sentence; // user input
        removeWhitespace();
        
        this.root = new Node(this.sentence);
        buildTree(root);
    }

    void buildTree(Node node) { // recursive function to build tree until all numbers are leaves
        node.parse();
        if (!node.leaf) { // every internal node will have a left and a right
            if (!node.left.leaf) {
                buildTree(node.left);
            }
            if (!node.right.leaf) {
                buildTree(node.right);
            }
        }
    }

    public double calculate(Node node) { // evaluates expression
        if (node.leaf) {
            return node.numValue;
        }

        // parse operator
        String operator = node.value;
        Double result = 0.0;
        if (operator.equals("+")) {
            result =  calculate(node.left) + calculate(node.right);
        } else if (operator.equals("-")) {
            result = calculate(node.left) - calculate(node.right);
        } else if (operator.equals("*")) {
            result = calculate(node.left) * calculate(node.right);
        } else if (operator.equals("/")) {
            result = calculate(node.left) / calculate(node.right);
        } else {
            System.out.println("Operator error with " + operator);
            return -1;
        }

        return result;
    }

    void removeWhitespace() { // removes whitespace
        this.sentence.replaceAll("\\s+","");
    }
}