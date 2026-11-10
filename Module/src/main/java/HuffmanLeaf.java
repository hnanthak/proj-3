public record HuffmanLeaf (int freq, char charASCII) implements HuffmanTree{


    @Override
    public boolean comesBefore(HuffmanTree other) {
        // unimplemented...
        //if the leaf has a smaller freq
        if(freq < getFreq(other)) {
            return true;
        }
        //freq equal and compare the ASCII values
        if(freq == getFreq(other)) {
            return charASCII < getChar(other);
        }
        //if not true then this does not become a tree
        return false;
    }


    @Override
    public HuffmanTree combine(HuffmanTree other) {
        // unimplemented...
        //stores the left and right children for the tree
        HuffmanTree left;
        HuffmanTree right;


        //puts the first tree to the left side
        if(comesBefore(other)) {
            left = this;
            right = other;
        } else {
            left = other;
            right = this;
        }
        //adds freq to both trees
        int newFreq = freq + getFreq(other);
        int newChar = Math.min((int) charASCII, getChar(other)); //finds the smaller ascii value
        return new HuffmanNode(newChar, newFreq, left, right); //returns the new parent node
        //return this;
    }


    @Override
    public int compareTo(HuffmanTree other) {
        // unimplemented ...
        //if the leaf comes before the other tree then this returns -1
        if(comesBefore(other)) {
            return -1;
        }
        //if the other tree combs before this leaf then it returns 1
        if(other.comesBefore(this)) {
            return 1;
        }
        //if both of the trees are equals and in order then return 0
        return 0;
    }


    //helper method??
    private int getFreq(HuffmanTree tree) {
        if(tree instanceof HuffmanLeaf leaf) {
            return leaf.freq();
        }
        return ((HuffmanNode) tree).freq();
    }


    private int getChar(HuffmanTree tree) {
        if(tree instanceof HuffmanLeaf leaf) {
            return leaf.charASCII();
        }
        return ((HuffmanNode) tree).charAscii();
    }
}
