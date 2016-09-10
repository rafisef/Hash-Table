import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Rafael on 4/29/2016.
 */

class HashTable{
    private class Node{
        private String key;
        private Node next;

        public Node(String key, Node next){
            this.key = key;
            this.next = next;
        }
    }
    Node[] buckets;
    Hasher hasher;
    public HashTable(int size, Hasher hasher){
        buckets = new Node[size];
        for(int i=0;i<buckets.length;i++){
            buckets[i] = null;
        }
        this.hasher = hasher;

    }

    public void add(String key){
        int index = hash(key);
        Node temp = buckets[index];

        while (temp !=null){
            if(key.equals(temp.key)){
                return;
            }
            else{
                temp = temp.next;
            }
        }
        buckets[index] = new Node(key,buckets[index]);
    }

    private int hash(String key){
        return Math.abs(hasher.hash(key)) % buckets.length;
    }

    public int keysAt(int index){ // number of keys in a index
        int count =0;
        Node temp = buckets[index];
        while(temp != null){
            count++;
            temp = temp.next;
        }
        return count;
    }

    public void writeStatistics(String title){
        int number=0;
        int smallestBucket = Integer.MAX_VALUE;
        int largestBucket = Integer.MIN_VALUE;
        int smallestIndex = Integer.MAX_VALUE;
        int largestIndex=-1;
        for(int i=0; i<buckets.length;i++){
            int a = keysAt(i);
            if(a>1){
                number++; // collisions
            }
            if(a < smallestBucket && a!=0){
                smallestBucket = a;
            }
            if(a > largestBucket){
                largestBucket = a;
            }
            if(a != 0){
                smallestIndex = Math.min(i,smallestIndex);
                largestIndex = i;
            }

        }
        //System.out.format("02d %d %d %d %d",title,number,smallestBucket,largestBucket);
        System.out.println(title);
        System.out.println("The number of collision: " + number);
        System.out.println("The smallest bucket " + smallestBucket);
        System.out.println("The largest bucket " + largestBucket);
        System.out.println("The smallest index of bucket " + smallestIndex);
        System.out.println("The largest index of bucket " + largestIndex);
        System.out.println();

    }
}

//  HASHER. A class whose instances provide a hash method HASH for STRINGs.

class Hasher
{
    public int hash(String key)
    {
        return 0;
    }
}

//  BITWISE HASHER. A bitwise hash method.

class BitwiseHasher extends Hasher
{
    public int hash(String key)
    {
        int bits = 0;
        for (int index = 0; index < key.length(); index += 1)
        {
            bits = (bits << 1) ^ key.charAt(index);
        }
        return bits;
    }
}

//  JAVA HASHER. Java's HASH CODE method.

class JavaHasher extends Hasher
{
    public int hash(String key)
    {
        return key.hashCode();
    }
}

//  LENGTH HASHER. Use the length of KEY as a hash code.

class LengthHasher extends Hasher
{
    public int hash(String key)
    {
        return key.length();
    }
}

//  SUM HASHER. A hash method that returns the sum of the CHARs of KEY.

class SumHasher extends Hasher
{
    public int hash(String key)
    {
        int sum = 0;
        for (int index = 0; index < key.length(); index += 1)
        {
            sum += key.charAt(index);
        }
        return sum;
    }
}

//  THIRTY SEVEN HASHER. A hash method that computes a base 37 polynomial whose
//  coefficients are the CHARs of KEY.

class ThirtySevenHasher extends Hasher
{
    public int hash(String key)
    {
        int sum = 0;
        for (int index = 0; index < key.length(); index += 1)
        {
            sum = 37 * sum + key.charAt(index);
        }
        return sum;
    }
}



//  WORDS. Iterator. Read words, represented as STRINGs, from a text file. Each
//  word is the longest possible contiguous series of non-whitespace CHARs.


public class CornedBeefHash {
    public static void main(String[] args)
    {
        HashTable bitwiseTable     = new HashTable(769, new BitwiseHasher());
        HashTable javaTable        = new HashTable(769, new JavaHasher());
        HashTable lengthTable      = new HashTable(769, new LengthHasher());
        HashTable sumTable         = new HashTable(769, new SumHasher());
        HashTable thirtySevenTable = new HashTable(769, new ThirtySevenHasher());

        //CornedBeefHash.Words words = new CornedBeefHash.Words(args[0]);
        Words words = new Words("C:\\Users\\Rafael\\Downloads\\hackers.txt");
        while (words.hasNext())
        {
            String word = words.next();
            bitwiseTable    .add(word);
            lengthTable     .add(word);
            javaTable       .add(word);
            sumTable        .add(word);
            thirtySevenTable.add(word);
        }

        bitwiseTable    .writeStatistics("BitwiseTable");
        javaTable       .writeStatistics("JavaTable");
        lengthTable     .writeStatistics("LengthTable");
        sumTable        .writeStatistics("SumTable");
        thirtySevenTable.writeStatistics("ThirtySevenTable");
    }

    public static class Words
    {
        private int           ch;      //  Last CHAR from READER, as an INT.
        private FileReader    reader;  //  Read CHARs from here.
        private StringBuilder word;    //  Last word read from READER.

    //  Constructor. Initialize an instance of WORDS, so it reads words from a file
    //  whose pathname is PATH. Throw an exception if we can't open PATH.

        public Words(String path)
        {
            try
            {
                reader = new FileReader(path);
                ch = reader.read();
            }
            catch (IOException ignore)
            {
                throw new IllegalArgumentException("Cannot open '" + path + "'.");
            }
        }

    //  HAS NEXT. Try to read a WORD from READER. Test if we were successful.

        public boolean hasNext()
        {
            word = new StringBuilder();
            while (ch > 0 && Character.isWhitespace((char) ch))
            {
                read();
            }
            while (ch > 0 && ! Character.isWhitespace((char) ch))
            {
                word.append((char) ch);
                read();
            }
            return word.length() > 0;
        }

    //  NEXT. If HAS NEXT is true, then return a WORD read from READER as a STRING.
    //  Otherwise, return an undefined STRING.

        public String next()
        {
            return word.toString();
        }

    //  READ. Read the next CHAR from READER. Set CH to the CHAR, represented as an
    //  INT. If there are no more CHARs to be read from READER, then set CH to -1.

        private void read()
        {
            try
            {
                ch = reader.read();
            }
            catch (IOException ignore)
            {
                ch = -1;
            }
        }

    //  MAIN. For testing. Open a text file whose pathname is the 0th argument from
    //  the command line. Read words from the file, and print them one per line.

        public static void main(String [] args)
        {
            Words words = new Words(args[0]);
            while (words.hasNext())
            {
                System.out.println("'" + words.next() + "'");
            }
        }
    }
}
/*
BitwiseTable
The number of collision: 480
The smallest bucket 1
The largest bucket 8
The smallest index of bucket 1
The largest index of bucket 766

JavaTable
The number of collision: 482
The smallest bucket 1
The largest bucket 8
The smallest index of bucket 1
The largest index of bucket 768

LengthTable
The number of collision: 18
The smallest bucket 1
The largest bucket 233
The smallest index of bucket 1
The largest index of bucket 25

SumTable
The number of collision: 440
The smallest bucket 1
The largest bucket 12
The smallest index of bucket 0
The largest index of bucket 768

ThirtySevenTable
The number of collision: 480
The smallest bucket 1
The largest bucket 7
The smallest index of bucket 0
The largest index of bucket 768


We know that for a hash function to be good it must try to uniformly map our keys such that the number of collisions are minimized.
Therefore using that definition we can see that ThirtySevenTable has the fewest collision containing at most 7 buckets.
The worst hashtable LengthTable because it clusters all the buckets between the index 1-25 with the maximum bucket length of 233.
This means that searching through the bucket would take longer since we have to go through a big linked list.
The other 3 tables also as good as ThirtySevenTable if not slightly worse since they have maximum buckets of size 8,8, and 12.
 */