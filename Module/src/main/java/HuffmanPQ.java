import java.util.PriorityQueue;

public class HuffmanPQ implements HQueue{
    private PriorityQueue<HuffmanTree> queue;
    public HuffmanPQ(){
        queue= new PriorityQueue<>();
    }
    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public int count() {
        return queue.size();
    }

    @Override
    public void enqueue(HuffmanTree tree) {
        queue.add(tree);
    }

    @Override
    public HuffmanTree dequeue() {
        return queue.poll();

    }
}
