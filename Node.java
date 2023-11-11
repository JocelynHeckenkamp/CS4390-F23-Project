class Node {
    String value;
    Node left;
    Node right;
    double numValue;
    boolean leaf;
    
    int operator;

    Node(String value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }

    public void parse() {
        // addition, subtraction
        for (int i = 0; i < this.value.length(); i++) {
            String c = this.value.substring(i, i + 1);
            if (c.equals("+") || c.equals("-")) {

                this.left = new Node(this.value.substring(0, i));
                this.right = new Node(this.value.substring(i + 1));
                this.value = this.value.substring(i, i + 1);
                this.leaf = false;

                return;
            }
        }

        // multiplication, division
        for (int i = 0; i < this.value.length(); i++) {
            String c = this.value.substring(i, i + 1);
            if (c.equals("*") || c.equals("/")) {
                this.left = new Node(this.value.substring(0, i));
                this.right = new Node(this.value.substring(i + 1));
                this.value = this.value.substring(i, i + 1);
                this.leaf = false;

                return;
            }
        }

        // leaf nodes
        this.numValue = Double.parseDouble(this.value);
        this.leaf = true;
    }
}