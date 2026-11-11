import org.junit.jupiter.api.Test;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class HuffmanTest {

    // Find sample files regardless of IntelliJ's working directory.
    private static Path testFile(String filename) {
        Path[] locations = {
                Paths.get("src", "test", "java", filename),
                Paths.get("Module", "src", "test", "java", filename),
                Paths.get(filename)
        };

        for (Path path : locations) {
            if (Files.isRegularFile(path)) {
                return path.toAbsolutePath();
            }
        }

        throw new IllegalArgumentException(
                "Cannot find " + filename
                        + ". Working directory: "
                        + System.getProperty("user.dir")
        );
    }

    // ================= ORIGINAL TESTS =================

    @Test
    public void testCntFreq() throws IOException {
        int[] freqList =
                Huffman.cntFreq(testFile("file2.txt").toString());

        int[] ansList = {2, 4, 8, 16, 0, 2, 0};

        for (int i = 0; i < ansList.length; i++) {
            assertEquals(ansList[i], freqList[97 + i],
                    "mismatch at ascii " + (97 + i));
        }
    }

    @Test
    public void testComesBefore() {
        HuffmanLeaf d = new HuffmanLeaf(5, 'd');
        HuffmanLeaf k = new HuffmanLeaf(5, 'k');

        assertTrue(d.comesBefore(k));
        assertFalse(k.comesBefore(d));
    }

    @Test
    public void testComesBeforeDiffFreq() {
        HuffmanLeaf a = new HuffmanLeaf(2, 'a');
        HuffmanLeaf b = new HuffmanLeaf(5, 'b');

        assertTrue(a.comesBefore(b));
        assertFalse(b.comesBefore(a));
    }

    @Test
    public void testCombine() {
        HuffmanLeaf a = new HuffmanLeaf(2, 'a');
        HuffmanLeaf b = new HuffmanLeaf(5, 'b');

        HuffmanNode result = (HuffmanNode) a.combine(b);

        assertEquals(7, result.freq());
        assertEquals(97, result.charAscii());
        assertEquals(a, result.left());
        assertEquals(b, result.right());
    }

    @Test
    public void testCompareTo() {
        HuffmanLeaf a = new HuffmanLeaf(2, 'a');
        HuffmanLeaf b = new HuffmanLeaf(5, 'b');

        assertTrue(a.compareTo(b) < 0);
        assertTrue(b.compareTo(a) > 0);
        assertEquals(0, a.compareTo(a));
    }

    @Test
    public void testNodeComesBefore() {
        HuffmanNode a = new HuffmanNode(97, 2, null, null);
        HuffmanNode b = new HuffmanNode(98, 5, null, null);

        assertTrue(a.comesBefore(b));
        assertFalse(b.comesBefore(a));
    }

    @Test
    public void testNodeCombine() {
        HuffmanNode a = new HuffmanNode(97, 2, null, null);
        HuffmanLeaf b = new HuffmanLeaf(5, 'b');

        HuffmanNode result = (HuffmanNode) a.combine(b);

        assertEquals(7, result.freq());
        assertEquals(97, result.charAscii());
        assertEquals(a, result.left());
        assertEquals(b, result.right());
    }

    @Test
    public void testCreateHeader() throws IOException {
        int[] freqList =
                Huffman.cntFreq(testFile("file2.txt").toString());

        assertEquals(
                "97 2 98 4 99 8 100 16 102 2",
                Huffman.createHeader(freqList)
        );
    }

    @Test
    public void test01Textfile() throws IOException {
        Path encoded = Files.createTempFile("huffman-file1-", ".txt");

        try {
            Huffman.huffmanEncode(
                    testFile("file1.txt").toString(),
                    encoded.toString()
            );

            assertEquals(
                    Files.readString(testFile("file1_soln.txt")).trim(),
                    Files.readString(encoded).trim()
            );
        } finally {
            Files.deleteIfExists(encoded);
        }
    }


    @Test
    public void testCreateHuffTree() {
        int[] freq = new int[256];
        freq[97] = 2;
        freq[98] = 3;

        HuffmanTree root = Huffman.createHuffTree(freq);

        assertInstanceOf(HuffmanNode.class, root);

        HuffmanNode node = (HuffmanNode) root;

        assertEquals(5, node.freq());
        assertEquals(2, ((HuffmanLeaf) node.left()).freq());
        assertEquals(3, ((HuffmanLeaf) node.right()).freq());
    }

    @Test
    public void testEmptyHuffTree() {
        int[] freq = new int[256];

        assertNull(Huffman.createHuffTree(freq));
    }

    @Test
    public void testSingleCharacterTree() {
        int[] freq = new int[256];
        freq[97] = 5;

        HuffmanTree root = Huffman.createHuffTree(freq);

        assertInstanceOf(HuffmanLeaf.class, root);
        assertEquals(5, ((HuffmanLeaf) root).freq());
    }



    @Test
    public void testCreateCode() {
        int[] freq = new int[256];
        freq[97] = 2;
        freq[98] = 3;

        HuffmanTree root = Huffman.createHuffTree(freq);
        String[] codes = Huffman.createCode(root);

        assertEquals("0", codes[97]);
        assertEquals("1", codes[98]);
        assertEquals("", codes[99]);
    }

    @Test
    public void testSingleCharacterCode() {
        int[] freq = new int[256];
        freq[97] = 5;

        HuffmanTree root = Huffman.createHuffTree(freq);
        String[] codes = Huffman.createCode(root);

        assertEquals("", codes[97]);
    }

    @Test
    public void testEmptyCode() {
        String[] codes = Huffman.createCode(null);

        assertEquals(256, codes.length);

        for (String code : codes) {
            assertEquals("", code);
        }
    }



    @Test
    public void testParseHeader() {
        int[] freq = Huffman.parseHeader("97 3 98 4 99 2");

        assertEquals(3, freq[97]);
        assertEquals(4, freq[98]);
        assertEquals(2, freq[99]);
        assertEquals(0, freq[100]);
    }

    @Test
    public void testEmptyHeader() {
        int[] freq = Huffman.parseHeader("");

        assertEquals(256, freq.length);

        for (int count : freq) {
            assertEquals(0, count);
        }
    }

    @Test
    public void testSingleCharacterHeader() {
        int[] freq = new int[256];
        freq[97] = 5;

        assertEquals("97 5", Huffman.createHeader(freq));
    }

    // ================= PRIORITY QUEUE TESTS =================

    @Test
    public void testHuffmanPQ() {
        HQueue queue = new HuffmanPQ();

        assertTrue(queue.isEmpty());
        assertEquals(0, queue.count());

        HuffmanLeaf a = new HuffmanLeaf(5, 'a');
        HuffmanLeaf b = new HuffmanLeaf(2, 'b');
        HuffmanLeaf c = new HuffmanLeaf(8, 'c');

        queue.enqueue(a);
        queue.enqueue(b);
        queue.enqueue(c);

        assertEquals(3, queue.count());
        assertFalse(queue.isEmpty());

        assertEquals(b, queue.dequeue());
        assertEquals(a, queue.dequeue());
        assertEquals(c, queue.dequeue());

        assertTrue(queue.isEmpty());
        assertEquals(0, queue.count());
    }

    @Test
    public void testPriorityQueueTie() {
        HQueue queue = new HuffmanPQ();

        HuffmanLeaf a = new HuffmanLeaf(5, 'a');
        HuffmanLeaf b = new HuffmanLeaf(5, 'b');

        queue.enqueue(b);
        queue.enqueue(a);

        assertEquals(a, queue.dequeue());
        assertEquals(b, queue.dequeue());
    }

    // ================= ENCODE / DECODE TESTS =================

    @Test
    public void testEncodeDecode() throws IOException {
        Path input = Files.createTempFile("input", ".txt");
        Path encoded = Files.createTempFile("encoded", ".txt");
        Path decoded = Files.createTempFile("decoded", ".txt");

        try {
            Files.writeString(input, "aaabbc");

            Huffman.huffmanEncode(
                    input.toString(), encoded.toString());

            Huffman.huffmanDecode(
                    encoded.toString(), decoded.toString());

            assertEquals(
                    Files.readString(input),
                    Files.readString(decoded)
            );
        } finally {
            Files.deleteIfExists(input);
            Files.deleteIfExists(encoded);
            Files.deleteIfExists(decoded);
        }
    }

    @Test
    public void testSingleCharacterEncodeDecode() throws IOException {
        Path input = Files.createTempFile("single", ".txt");
        Path encoded = Files.createTempFile("encoded", ".txt");
        Path decoded = Files.createTempFile("decoded", ".txt");

        try {
            Files.writeString(input, "aaaaa");

            Huffman.huffmanEncode(
                    input.toString(), encoded.toString());

            assertEquals("97 5\n", Files.readString(encoded));

            Huffman.huffmanDecode(
                    encoded.toString(), decoded.toString());

            assertEquals("aaaaa", Files.readString(decoded));
        } finally {
            Files.deleteIfExists(input);
            Files.deleteIfExists(encoded);
            Files.deleteIfExists(decoded);
        }
    }

    @Test
    public void testEmptyFile() throws IOException {
        Path input = Files.createTempFile("empty", ".txt");
        Path encoded = Files.createTempFile("encoded", ".txt");
        Path decoded = Files.createTempFile("decoded", ".txt");

        try {
            Huffman.huffmanEncode(
                    input.toString(), encoded.toString());

            assertEquals(0, Files.size(encoded));

            Huffman.huffmanDecode(
                    encoded.toString(), decoded.toString());

            assertEquals(0, Files.size(decoded));
        } finally {
            Files.deleteIfExists(input);
            Files.deleteIfExists(encoded);
            Files.deleteIfExists(decoded);
        }
    }

    @Test
    public void testMissingInputFile() throws IOException {
        Path directory = Files.createTempDirectory("huffman-missing");

        try {
            String missingFile =
                    directory.resolve("missing.txt").toString();
            String outputFile =
                    directory.resolve("output.txt").toString();

            assertThrows(FileNotFoundException.class, () -> {
                Huffman.huffmanEncode(missingFile, outputFile);
            });
        } finally {
            Files.deleteIfExists(directory.resolve("output.txt"));
            Files.deleteIfExists(directory);
        }
    }

    @Test
    public void testMissingEncodedFile() throws IOException {
        Path directory = Files.createTempDirectory("huffman-missing");

        try {
            String missingFile =
                    directory.resolve("missing.txt").toString();
            String decodedFile =
                    directory.resolve("decoded.txt").toString();

            assertThrows(FileNotFoundException.class, () -> {
                Huffman.huffmanDecode(missingFile, decodedFile);
            });
        } finally {
            Files.deleteIfExists(directory.resolve("decoded.txt"));
            Files.deleteIfExists(directory);
        }
    }

    @Test
    public void testEncodeDecodeWithSpacesAndNewlines()
            throws IOException {

        Path input = Files.createTempFile("input", ".txt");
        Path encoded = Files.createTempFile("encoded", ".txt");
        Path decoded = Files.createTempFile("decoded", ".txt");

        try {
            Files.writeString(input, "hello world!\nhello again!");

            Huffman.huffmanEncode(
                    input.toString(), encoded.toString());

            Huffman.huffmanDecode(
                    encoded.toString(), decoded.toString());

            assertEquals(
                    Files.readString(input),
                    Files.readString(decoded)
            );
        } finally {
            Files.deleteIfExists(input);
            Files.deleteIfExists(encoded);
            Files.deleteIfExists(decoded);
        }
    }

    @Test
    public void testOverwriteOutputFile() throws IOException {
        Path input = Files.createTempFile("input", ".txt");
        Path encoded = Files.createTempFile("encoded", ".txt");
        Path decoded = Files.createTempFile("decoded", ".txt");

        try {
            Files.writeString(input, "abcabc");
            Files.writeString(encoded, "OLD ENCODED CONTENT");
            Files.writeString(decoded, "OLD DECODED CONTENT");

            Huffman.huffmanEncode(
                    input.toString(), encoded.toString());

            Huffman.huffmanDecode(
                    encoded.toString(), decoded.toString());

            assertEquals("abcabc", Files.readString(decoded));

            assertFalse(
                    Files.readString(encoded)
                            .contains("OLD ENCODED CONTENT")
            );
        } finally {
            Files.deleteIfExists(input);
            Files.deleteIfExists(encoded);
            Files.deleteIfExists(decoded);
        }
    }
}
