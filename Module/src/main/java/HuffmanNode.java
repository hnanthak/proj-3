
/**
 * A single node in a Huffman tree, implemented as a record.
 *
 * A HuffmanNode represents either a leaf (an actual character together
 * with its occurrence count) or an internal node created by combining two
 * subtrees during tree construction.
 *
 * Records are immutable and give you charAscii(), freq(), left(), and
 * right() accessor methods automatically, along with
 * equals()/hashCode()/toString(). Do not rename the components below.
 */
public record HuffmanNode(int charAscii, int freq, HuffmanTree left, HuffmanTree right)
        implements HuffmanTree{
    public boolean comesBefore(HuffmanTree other) {
        // TODO: implement
        //to store the trees freq and ascii value
        int otherFreq;
        int otherChar;
        //gets the value if the other tree is a leaf instead
        if(other instanceof HuffmanLeaf leaf) {
            otherFreq = leaf.freq();
            otherChar = leaf.charASCII();
        } else { //if the other tree is a node then its gets the values
            HuffmanNode node = (HuffmanNode) other;
            otherFreq = node.freq();
            otherChar = node.charAscii();
        }
        //checks if this node has a smaller freq
        if(freq < otherFreq) {
            return true;
        }
        //if the freqs are equals then it compares the ascii values
        if(freq == otherFreq) {
            return charAscii < otherChar;
        }
        //if none true then this node does not come before the other tree
        return false;
    }


    public HuffmanTree combine(HuffmanTree other) {
        // TODO: implement
        //to store the new left and right children
        HuffmanTree newLeft;
        HuffmanTree newRight;
        //puts the tree that comes first
        if(comesBefore(other)) {
            newLeft = this;
            newRight = other;
        } else {
            newLeft = other;
            newRight = this;
        }
        //gets other trees freq and ascii value
        int otherFreq;
        int otherChar;
        if(other instanceof HuffmanLeaf leaf) {
            otherFreq = leaf.freq();
            otherChar = leaf.charASCII();
        } else {
            HuffmanNode node = (HuffmanNode) other;
            otherFreq = node.freq();
            otherChar = node.charAscii();
        }
        //add freq on both trees
        int newFreq = freq + otherFreq;
        //looks for the smaller ascii value
        int newChar = Math.min(charAscii, otherChar);
        //the new parent node is returned
        return new HuffmanNode(newChar, newFreq, newLeft, newRight);
        //return null;
    }


    /**
     * Natural ordering consistent with comesBefore(), so that HuffmanNode
     * objects can be stored directly in a sorted structure (e.g. a
     * PriorityQueue or a sorted List) while building the tree.
     */
    @Override
    public int compareTo(HuffmanTree other) {
        //return -1 if this node comes before the other tree
        if (this.comesBefore(other))
            return -1;
        //return 1 if the other node comes before this node
        if (other.comesBefore(this))
            return 1;
        //return 0 if both of the trees are equal and in order
        return 0;
    }
}

