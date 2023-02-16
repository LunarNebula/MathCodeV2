package DataSet;

import Algebra.BooleanSetOperable;
import DataKey.Index;
import General.TrueTextEncodable;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * A Binary Search Tree sorting object
 * @param <Data> the Comparable target object for sorting
 */
public class BST<Data extends Comparable<Data>> implements DataSet<Data>, BooleanSetOperable<BST<Data>>, TrueTextEncodable {
    private Data data;
    private BST<Data> above, below;
    private int size, quantity;

    /**
     * Creates an empty BST
     */
    public BST() {
        this.size = 0;
        this.quantity = 0;
    }

    /**
     * Creates a new BST and preloads an array of Data
     * @param data an array of Data
     */
    @SafeVarargs
    public BST(Data... data) {
        this.size = 0;
        this.quantity = 0;
        insertAll(data);
    }

    /**
     * Creates a new BST and preloads a List of Data
     * @param data a List of Data
     */
    public BST(Iterable<Data> data) {
        this.size = 0;
        this.quantity = 0;
        insertAll(data);
    }

    /**
     * Creates a new BST and preloads the contents of an existing BST
     * @param bst the model BST
     */
    public BST(BST<Data> bst) {
        if(bst == null) {
            this.size = 0;
            this.quantity = 0;
        } else {
            Stack<BST<Data>> thisStack = new Stack<>();
            Stack<BST<Data>> bstStack = new Stack<>();
            thisStack.push(this);
            bstStack.push(bst);
            while(! thisStack.empty()) {
                BST<Data> targetThis = thisStack.pop();
                BST<Data> targetBST = bstStack.pop();
                targetThis.data = targetBST.data;
                targetThis.size = targetBST.size;
                targetThis.quantity = targetBST.quantity;
                if(targetBST.above != null) {
                    BST<Data> above = new BST<Data>();
                    thisStack.push(above);
                    targetThis.above = above;
                    bstStack.push(targetBST.above);
                }
                if(targetBST.below != null) {
                    BST<Data> below = new BST<Data>();
                    thisStack.push(below);
                    targetThis.below = below;
                    bstStack.push(targetBST.below);
                }
            }
        }
    }

    /**
     * Inserts an element of Data into this BST
     * @param data the data to be added
     */
    public void insert(Data data) {
        if(data != null) {
            if(this.data == null) {
                this.data = data;
                this.size = 1;
                this.quantity = 1;
            } else {
                BST<Data> cursor = this, prevCursor = cursor;
                while(cursor != null && ! cursor.data.equals(data)) {
                    prevCursor = cursor;
                    prevCursor.size++;
                    cursor = (cursor.data.compareTo(data) < 0) ? cursor.above : cursor.below;
                }
                if(cursor == null) {
                    cursor = new BST<Data>();
                    cursor.data = data;
                    cursor.size = 1;
                    cursor.quantity = 1;
                    if(prevCursor.data.compareTo(data) < 0) {
                        prevCursor.above = cursor;
                    } else {
                        prevCursor.below = cursor;
                    }
                } else {
                    cursor.size++;
                    cursor.quantity++;
                }
            }
        }
    }

    /**
     * Inserts all elements of a Data array into this BST
     * @param data an array of Data
     */
    @SafeVarargs
    public final void insertAll(Data ... data) {
        if(data != null) {
            for(Data element : data) {
                insert(element);
            }
        }
    }

    /**
     * Inserts all elements of a Data list into this BST
     * @param data an Iterable List of Data
     */
    public void insertAll(Iterable<Data> data) {
        if(data != null) {
            for(Data element : data) {
                insert(element);
            }
        }
    }

    /**
     * Inserts all elements of another BST into this BST
     * @param bst the addend BST
     */
    public void insertAll(BST<Data> bst) {
        if(bst != null) {
            Stack<BST<Data>> stack = new Stack<>();
            if(bst.data != null) {
                stack.push(bst);
            }
            while(! stack.isEmpty()) {
                BST<Data> cursor = stack.pop();
                insert(cursor.data);
                if(cursor.below != null) {
                    stack.push(cursor.below);
                }
                if(cursor.above != null) {
                    stack.push(cursor.above);
                }
            }
        }
    }

    /**
     * Gets a complete, sorted List of Data from this BST
     * @return the complete List of Data
     */
    public List<Data> getOrderedList() {
        List<Data> orderedList = new LinkedList<>();
        if(this.data == null) {
            return orderedList;
        }
        Stack<BST<Data>> stack = new Stack<>();
        stack.push(this);
        Stack<Boolean> record = new Stack<>();
        record.push(false);
        while(! stack.isEmpty()) {
            BST<Data> cursor = stack.pop();
            if(record.pop()) {
                for(int i = 0; i < cursor.quantity; i++) {
                    orderedList.add(cursor.data);
                }
                if(cursor.above != null) {
                    stack.push(cursor.above);
                    record.push(false);
                }
            } else {
                stack.push(cursor);
                record.push(true);
                if(cursor.below != null) {
                    stack.push(cursor.below);
                    record.push(false);
                }
            }
        }
        return orderedList;
    }

    /**
     * Finds the minimum Value in this BST
     * @return the Value 'o' in this BST such that for all values 't' in this BST, 'o' <= 't'
     */
    @Override
    public Data min() {
        BST<Data> cursor = this;
        while(cursor.below != null) {
            cursor = cursor.below;
        }
        return cursor.data;
    }

    /**
     * Removes the smallest Data object from this BST
     * @return the smallest Data object
     */
    public Data removeMin() {
        if(this.data == null) {
            return null;
        }
        BST<Data> cursor = this, prevCursor = cursor;
        while(cursor.below != null) {
            prevCursor = cursor;
            cursor.size--;
            cursor = cursor.below;
        }
        cursor.size--;
        cursor.quantity--;
        Data ejected = cursor.data;
        if(this.size == 0) {
            cursor.data = null;
            cursor.quantity = 0;
        } else if(cursor.quantity == 0) {
            if(cursor.above == null) {
                prevCursor.below = null;
            } else {
                cursor.data = cursor.above.data;
                cursor.below = cursor.above.below;
                cursor.quantity = cursor.above.quantity;
                cursor.above = cursor.above.above;
            }
        }
        return ejected;
    }

    /**
     * Removes all instances of the current minimum Data value in this
     * @return an Index containing the minimum value and the number of instances of that value in this BST
     */
    @Nullable
    private Index<Data, Integer> removeAllMin() {
        if(this.data == null) {
            return null;
        }
        BST<Data> cursor = this, prevCursor = cursor;
        Stack<BST<Data>> stack = new Stack<>();
        while(cursor.below != null) {
            prevCursor = cursor;
            cursor.size--;
            stack.push(cursor);
            cursor = cursor.below;
        }
        cursor.size -= cursor.quantity;
        Index<Data, Integer> ejected = new Index<>(cursor.data, cursor.quantity);
        if(cursor.above == null) {
            prevCursor.below = null;
        } else {
            cursor.data = cursor.above.data;
            cursor.below = cursor.above.below;
            cursor.quantity = cursor.above.quantity;
            cursor.above = cursor.above.above;
        }
        while(! stack.isEmpty()) {
            stack.pop().size -= ejected.b();
        }
        return ejected;
    }

    /**
     * Finds the maximum Value in this BST
     * @return the Value 'o' in this BST such that for all values 't' in this BST, 'o' >= 't'
     */
    @Override
    public Data max() {
        BST<Data> cursor = this;
        while(cursor.above != null) {
            cursor = cursor.above;
        }
        return cursor.data;
    }

    /**
     * Removes the largest Data object from this BST
     * @return the largest Data object
     */
    public Data removeMax() {
        if(this.data == null) {
            return null;
        }
        BST<Data> cursor = this, prevCursor = cursor;
        while(cursor.above != null) {
            prevCursor = cursor;
            cursor.size--;
            cursor = cursor.above;
        }
        cursor.size--;
        cursor.quantity--;
        Data ejected = cursor.data;
        if(this.size == 0) {
            cursor.data = null;
            cursor.quantity = 0;
        } else if(cursor.quantity == 0) {
            if(cursor.below == null) {
                prevCursor.above = null;
            } else {
                cursor.data = cursor.below.data;
                cursor.above = cursor.below.above;
                cursor.below = cursor.below.below;
            }
        }
        return ejected;
    }

    /**
     * Removes all instances of the current maximum Data value in this
     * @return an Index containing the maximum value and the number of instances of that value in this BST
     */
    @Nullable
    private Index<Data, Integer> removeAllMax() {
        if(this.data == null) {
            return null;
        }
        BST<Data> cursor = this, prevCursor = cursor;
        Stack<BST<Data>> stack = new Stack<>();
        while(cursor.above != null) {
            prevCursor = cursor;
            cursor.size--;
            stack.push(cursor);
            cursor = cursor.above;
        }
        cursor.size -= cursor.quantity;
        Index<Data, Integer> ejected = new Index<>(cursor.data, cursor.quantity);
        if(cursor.below == null) {
            prevCursor.above = null;
        } else {
            cursor.data = cursor.below.data;
            cursor.above = cursor.below.below;
            cursor.quantity = cursor.below.quantity;
            cursor.below = cursor.below.below;
        }
        while(! stack.isEmpty()) {
            stack.pop().size -= ejected.b();
        }
        return ejected;
    }

    /**
     * Attempts to remove a Data object from this BST
     * @param data the target object
     * @return true if the removal is successful, else false if no such element occurs in this BST
     */
    @Override
    public boolean remove(Data data) {
        if(this.data == null) {
            return false;
        }
        BST<Data> cursor = this;
        Stack<BST<Data>> stack = new Stack<>();
        while(! (cursor == null || cursor.data.equals(data))) {
            stack.push(cursor);
            cursor = (cursor.data.compareTo(data) < 0) ? cursor.above : cursor.below;
        }
        if(cursor == null) {
            return false;
        }
        cursor.size--;
        if(cursor.quantity == 1) {
            int aboveSize = (cursor.above == null) ? 0 : cursor.above.size;
            int belowSize = (cursor.below == null) ? 0 : cursor.below.size;
            if(aboveSize > belowSize) {
                cursor.data = cursor.above.removeMin();
            } else if(belowSize > 0) {
                cursor.data = cursor.below.removeMax();
            } else {
                cursor.data = null;
            }
        } else {
            cursor.quantity--;
        }
        cursor.clean();
        if(! stack.isEmpty()) {
            cursor = stack.pop();
            cursor.clean();
            cursor.size--;
            while(! stack.isEmpty()) {
                cursor = stack.pop();
                cursor.size--;
            }
        }
        return true;
    }

    /**
     * Clears any null children from this BST node
     */
    private void clean() {
        if(this.below != null) {
            if(this.below.data == null) {
                this.below = null;
            }
        }
        if(this.above != null) {
            if(this.above.data == null) {
                this.above = null;
            }
        }
    }

    /**
     * Determines whether this BST contains a specific Data object
     * @param data the Data object
     * @return true if this BST contains the object, else false
     */
    @Override
    public boolean contains(Data data) {
        if(this.data == null || data == null) {
            return false;
        }
        BST<Data> cursor = this;
        while(! (cursor == null || cursor.data.equals(data))) {
            cursor = (cursor.data.compareTo(data) < 0) ? cursor.above : cursor.below;
        }
        return cursor != null;
    }

    /**
     * Gets the total quantity of a Data object in this BST
     * @param data the target object
     * @return the number of occurrences of this object
     */
    public int getQuantity(Data data) {
        BST<Data> cursor = this;
        while(! (cursor == null || cursor.data == null)) {
            if(cursor.data.equals(data)) {
                return cursor.quantity;
            }
            cursor = (cursor.data.compareTo(data) < 0) ? cursor.above : cursor.below;
        }
        return 0;
    }

    /**
     * Removes all instances of a Data object from this BST
     * @param data the target object
     * @return the number of instances of the target object previously contained in this BST
     */
    @Override
    public int removeAll(Data data) {
        if(this.data == null) {
            return 0;
        }
        BST<Data> cursor = this;
        Stack<BST<Data>> stack = new Stack<>();
        while(! (cursor == null || cursor.data.equals(data))) {
            stack.push(cursor);
            cursor = (cursor.data.compareTo(data) < 0) ? cursor.above : cursor.below;
        }
        if(cursor == null) {
            return 0;
        }
        int quantity = cursor.quantity;
        cursor.size -= cursor.quantity;
        int aboveSize = (cursor.above == null) ? 0 : cursor.above.size;
        int belowSize = (cursor.below == null) ? 0 : cursor.below.size;
        if(aboveSize > belowSize) {
            cursor.data = cursor.above.removeMin();
        } else if(belowSize > 0) {
            cursor.data = cursor.below.removeMax();
        } else {
            cursor.data = null;
        }
        cursor.clean();
        if(! stack.isEmpty()) {
            cursor = stack.pop();
            cursor.clean();
            cursor.size -= cursor.quantity;
            while(! stack.isEmpty()) {
                cursor = stack.pop();
                cursor.size -= cursor.quantity;
            }
        }
        return quantity;
    }

    /**
     * Creates a deep copy of this BST
     * @return the new copy
     */
    public BST<Data> copy() {
        BST<Data> copy = new BST<Data>();
        Stack<BST<Data>> thisStack = new Stack<>(), copyStack = new Stack<>();
        thisStack.push(this);
        copyStack.push(copy);
        while(! thisStack.isEmpty()) {
            BST<Data> thisCursor = thisStack.pop(), copyCursor = copyStack.pop();
            copyCursor.data = thisCursor.data;
            copyCursor.size = thisCursor.size;
            copyCursor.quantity = thisCursor.quantity;
            if(thisCursor.below != null) {
                thisStack.push(thisCursor.below);
                copyCursor.below = new BST<Data>();
                copyStack.push(copyCursor.below);
            }
            if(thisCursor.above != null) {
                thisStack.push(thisCursor.above);
                copyCursor.above = new BST<Data>();
                copyStack.push(copyCursor.above);
            }
        }
        return copy;
    }

    /**
     * Finds all modes of this DataSet
     * @return a List of all modes: let c(p) equal the number of occurrences of 'p' in this DataSet. For all elements
     * 'e' in this DataSet, a mode 'm' of this DataGroup satisfies c(m) >= c(e)
     */
    @Override
    public List<Data> modes() {
        Stack<BST<Data>> stack = new Stack<>();
        if(this.data != null) {
            stack.push(this);
        }
        final List<Data> modes = new LinkedList<>();
        Integer maxCount = 0;
        while(! stack.isEmpty()) {
            BST<Data> cursor = stack.pop();
            if(cursor.above != null) {
                stack.push(cursor.above);
            }
            if(cursor.below != null) {
                stack.push(cursor.below);
            }
            int compare = maxCount.compareTo(cursor.quantity);
            if(compare <= 0) {
                if(compare < 0) {
                    modes.clear();
                    maxCount = cursor.quantity;
                }
                modes.add(cursor.data);
            }
        }
        return modes;
    }

    /**
     * Balances this BST
     */
    public void balance() {
        BST<Data> balancedCopy = getBSTFromOrderedList(getOrderedList());
        this.data = balancedCopy.data;
        this.below = balancedCopy.below;
        this.above = balancedCopy.above;
        this.quantity = balancedCopy.quantity;
    }

    /**
     * Creates a new BST from the intersection of the datasets of two BSTs
     * @param bst the comparator BST
     * @return the intersection BST for this and the comparator
     */
    @Override
    public BST<Data> AND(BST<Data> bst) {
        if(bst == null) {
            return new BST<Data>();
        }
        final int THIS_INDEX = 0, BST_INDEX = 1, RETURN_INDEX = 2;
        List<Stack<BST<Data>>> stack = List.of(new Stack<>(), new Stack<>());
        List<Stack<Boolean>> record = List.of(new Stack<>(), new Stack<>());
        List<LinkedQueue<Data>> queue = List.of(new LinkedQueue<Data>(), new LinkedQueue<Data>(), new LinkedQueue<Data>());
        List<LinkedQueue<Integer>> count = List.of(new LinkedQueue<Integer>(), new LinkedQueue<Integer>());
        Data thisData = null, bstData = null;
        Integer thisCount = null, bstCount = null;
        boolean stacksContainMoreData = this.data != null;
        if(stacksContainMoreData) {
            stack.get(THIS_INDEX).push(this);
            record.get(THIS_INDEX).push(false);
            stack.get(BST_INDEX).push(bst);
            record.get(BST_INDEX).push(false);
        }
        while(stacksContainMoreData) {
            stacksContainMoreData = false;
            boolean currentTreeFocusIsThisTree = false;
            do {
                int index = currentTreeFocusIsThisTree ? THIS_INDEX : BST_INDEX;
                Stack<BST<Data>> stackFocus = stack.get(index);
                Stack<Boolean> recordFocus = record.get(index);
                LinkedQueue<Data> queueFocus = queue.get(index);
                LinkedQueue<Integer> countFocus = count.get(index);
                if(! stackFocus.isEmpty()) {
                    stacksContainMoreData = true;
                    BST<Data> cursor = stackFocus.pop();
                    if(recordFocus.pop()) {
                        queueFocus.add(cursor.data);
                        countFocus.add(cursor.quantity);
                        if(cursor.above != null) {
                            stackFocus.push(cursor.above);
                            recordFocus.push(false);
                        }
                    } else {
                        stackFocus.add(cursor);
                        recordFocus.add(true);
                        if(cursor.below != null) {
                            stackFocus.add(cursor.below);
                            recordFocus.add(false);
                        }
                    }
                }
                currentTreeFocusIsThisTree = ! currentTreeFocusIsThisTree;
            } while(currentTreeFocusIsThisTree);
            while(! (queue.get(THIS_INDEX).isEmpty() || queue.get(BST_INDEX).isEmpty())) {
                if(thisData == bstData || thisData.equals(bstData)) {
                    if(thisData != null) {
                        final int maxCount = Math.min(thisCount, bstCount);
                        for(int i = 0; i < maxCount; i++) {
                            queue.get(RETURN_INDEX).add(thisData);
                        }
                    }
                    thisData = queue.get(THIS_INDEX).poll();
                    thisCount = count.get(THIS_INDEX).poll();
                    bstData = queue.get(BST_INDEX).poll();
                    bstCount = count.get(BST_INDEX).poll();
                } else if(thisData.compareTo(bstData) < 0) {
                    thisData = queue.get(THIS_INDEX).poll();
                    thisCount = count.get(THIS_INDEX).poll();
                } else {
                    bstData = queue.get(BST_INDEX).poll();
                    bstCount = count.get(BST_INDEX).poll();
                }
            }
        }
        if(thisData != null && thisData.equals(bstData)) {
            final int maxCount = Math.min(thisCount, bstCount);
            for(int i = 0; i < maxCount; i++) {
                queue.get(RETURN_INDEX).add(thisData);
            }
        }
        return getBSTFromOrderedList(queue.get(RETURN_INDEX));
    }

    /**
     * Creates a new BST from the union of the datasets of two BSTs
     * @param bst the comparator BST
     * @return the union BST for this and the comparator
     */
    @Override
    public BST<Data> OR(BST<Data> bst) {
        if(bst == null) {
            return copy();
        }
        List<Data> thisOrdered = getOrderedList(), bstOrdered = bst.getOrderedList();
        LinkedQueue<Data> newOrdered = new LinkedQueue<Data>();
        ListIterator<Data> thisIterator = thisOrdered.listIterator(), bstIterator = bstOrdered.listIterator();
        Data t = null, b = null;
        while(thisIterator.hasNext() || bstIterator.hasNext()) {
            if(t == b || t.equals(b)) {
                if(t != null) {
                    newOrdered.add(t);
                }
                t = thisIterator.next();
                b = bstIterator.next();
            } else if(t.compareTo(b) > 0) {
                newOrdered.add(b);
                b = bstIterator.next();
            } else {
                newOrdered.add(t);
                t = thisIterator.next();
            }
        }
        if(t == null || b == null) {
            if(t != null) {
                newOrdered.add(t);
            } else if(b != null) {
                newOrdered.add(b);
            }
        } else if(t.compareTo(b) > 0) {
            newOrdered.add(b);
            newOrdered.add(t);
        } else {
            newOrdered.add(t);
            if(t.compareTo(b) < 0) {
                newOrdered.add(b);
            }
        }
        return getBSTFromOrderedList(newOrdered);
    }

    /**
     * Creates a new BST from the disjunctive union of the datasets of two BSTs
     * @param bst the comparator BST
     * @return the disjunctive union BST for this and the comparator
     */
    @Override
    public BST<Data> XOR(BST<Data> bst) {
        if(bst == null) {
            return copy();
        }
        List<Data> thisOrdered = getOrderedList(), bstOrdered = bst.getOrderedList();
        LinkedQueue<Data> newOrdered = new LinkedQueue<Data>();
        ListIterator<Data> thisIterator = thisOrdered.listIterator(), bstIterator = bstOrdered.listIterator();
        Data t = null, b = null;
        while(thisIterator.hasNext() && bstIterator.hasNext()) {
            if(t == b || t.equals(b)) {
                t = thisIterator.next();
                b = bstIterator.next();
            } else if(t.compareTo(b) > 0) {
                newOrdered.add(b);
                b = bstIterator.next();
            } else {
                newOrdered.add(t);
                t = thisIterator.next();
            }
        }
        while(thisIterator.hasNext()) {
            newOrdered.add(thisIterator.next());
        }
        while(bstIterator.hasNext()) {
            newOrdered.add(bstIterator.next());
        }
        return getBSTFromOrderedList(newOrdered);
    }

    /**
     * Creates a new BST with all elements contained in this BST and not in another BST
     * @param bst the comparator BST
     * @return the new exclusive BST
     */
    @Override
    public BST<Data> AND_NOT(BST<Data> bst) {
        BST<Data> AND_NOT = copy();
        if(bst != null) {
            for(Data data : bst.getOrderedList()) {
                AND_NOT.remove(data);
            }
            AND_NOT.balance();
        }
        return AND_NOT; //TODO: rewrite with O(n) complexity using iterator-sortedlist method below
    }

    public BST<Data> AND_NOT_TEST(BST<Data> bst) {
        final int THIS_INDEX = 0, BST_INDEX = 1, RETURN_INDEX = 2;
        List<Stack<BST<Data>>> stack = List.of(new Stack<>(), new Stack<>());
        List<Stack<Boolean>> record = List.of(new Stack<>(), new Stack<>());
        List<LinkedQueue<Data>> queue = List.of(new LinkedQueue<Data>(), new LinkedQueue<Data>(), new LinkedQueue<Data>());
        List<LinkedQueue<Integer>> count = List.of(new LinkedQueue<Integer>(), new LinkedQueue<Integer>(), new LinkedQueue<Integer>());
        if(this.data != null) {
            stack.get(THIS_INDEX).push(this);
            stack.get(BST_INDEX).push(bst);
            record.get(THIS_INDEX).push(false);
            record.get(BST_INDEX).push(false);
        }
        Data thisData = null, bstData = null;
        int thisCount = -1, bstCount = -1;
        boolean stackContainsMoreValues = ! (stack.get(THIS_INDEX).isEmpty() && stack.get(BST_INDEX).isEmpty());
        while(stackContainsMoreValues) {
            stackContainsMoreValues = false;
            boolean isConsideringThisSearchTree = false;
            do {
                final int INDEX = isConsideringThisSearchTree ? THIS_INDEX : BST_INDEX;
                Stack<BST<Data>> stackFocus = stack.get(INDEX);
                if(! stackFocus.isEmpty()) {
                    stackContainsMoreValues = true;
                    Stack<Boolean> recordFocus = record.get(INDEX);
                    LinkedQueue<Data> queueFocus = queue.get(INDEX);
                    LinkedQueue<Integer> countFocus = count.get(INDEX);
                    BST<Data> cursor = stackFocus.pop();
                    if(recordFocus.pop()) {
                        queueFocus.add(cursor.data);
                        countFocus.add(cursor.quantity);
                        if(cursor.above != null) {
                            stackFocus.push(cursor.above);
                            recordFocus.push(false);
                        }
                    } else {
                        stackFocus.push(cursor);
                        recordFocus.push(true);
                        if(cursor.below != null) {
                            stackFocus.push(cursor.below);
                            recordFocus.push(false);
                        }
                    }
                }
                isConsideringThisSearchTree = ! isConsideringThisSearchTree;
            } while(isConsideringThisSearchTree);
            while(! queue.get(THIS_INDEX).isEmpty()) {
                if(thisData == bstData || thisData.equals(bstData)) {
                    thisData = queue.get(THIS_INDEX).poll();
                    bstData = queue.get(BST_INDEX).poll();
                } else if(bstData == null) {
                    queue.get(RETURN_INDEX).add(thisData);
                    count.get(RETURN_INDEX).add(thisCount);
                }
            }
        }
        return getBSTFromOrderedList(queue.get(RETURN_INDEX));
    }

    /**
     * Converts an ordered Iterable list into a BST
     * @param list the given list of Data values
     * @return the (approximately) balanced BST
     */
    private BST<Data> getBSTFromOrderedList(Iterable<Data> list) {
        Stack<BST<Data>> bstStack = new Stack<>();
        Stack<Boolean> recordStack = new Stack<>();
        Stack<Integer> layerStack = new Stack<>(), maxLayerStack = new Stack<>();
        bstStack.push(new BST<Data>());
        recordStack.push(true);
        layerStack.push(0);
        maxLayerStack.push(0);
        Data prevData = null;
        int count = 0;
        for(Data data : list) {
            if(prevData == null) {
                prevData = data;
                count = 1;
            } else if(prevData.equals(data)) {
                count++;
            } else {
                BST<Data> bstCursor = bstStack.pop();
                bstCursor.data = prevData;
                bstCursor.quantity = count;
                prevData = data;
                count = 1;
                int layer = layerStack.pop(), maxLayer = maxLayerStack.pop();
                if(recordStack.pop()) {
                    BST<Data> connectorCell = new BST<Data>();
                    connectorCell.below = bstCursor.below;
                    connectorCell.above = bstCursor.above;
                    connectorCell.data = bstCursor.data;
                    connectorCell.quantity = bstCursor.quantity;
                    bstCursor.below = connectorCell;
                    bstCursor.data = null;
                    bstCursor.above = null;
                    bstStack.push(bstCursor);
                    recordStack.push(layer + 1 != maxLayer);
                    layerStack.push(layer + 1);
                    maxLayerStack.push(layer == maxLayer ? (maxLayer + 1) : maxLayer);
                    if(layer > 0) {
                        if(connectorCell.above == null) {
                            connectorCell.above = new BST<Data>();
                        }
                        bstStack.push(connectorCell.above);
                        recordStack.push(layer != 1);
                        layerStack.push(0);
                        maxLayerStack.push(layer - 1);
                    }
                } else if(maxLayer > 0) {
                    bstCursor.above = new BST<Data>();
                    bstStack.push(bstCursor.above);
                    recordStack.push(layer > 1);
                    layerStack.push(0);
                    maxLayerStack.push(maxLayer - 1);
                }
            }
        }
        BST<Data> lastItem = bstStack.peek();
        lastItem.data = prevData;
        lastItem.quantity = count;
        while(bstStack.size() > 1) {
            bstStack.pop();
        }
        BST<Data> bst = bstStack.pop(), cursor = bst, prevCursor = cursor;
        while(cursor != null) {
            if(cursor.data == null) {
                if(cursor.below == null) {
                    prevCursor.above = null;
                } else {
                    cursor.above = cursor.below.above;
                    cursor.data = cursor.below.data;
                    cursor.quantity = cursor.below.quantity;
                    cursor.below = cursor.below.below;
                }
            }
            prevCursor = cursor;
            cursor = cursor.above;
        }
        assert bst != null;
        bst.updateSizes();
        return bst;
    }

    /**
     * Updates the values of all size fields in this BST
     */
    private void updateSizes() {
        Stack<BST<Data>> stack = new Stack<>();
        Stack<Boolean> record = new Stack<>();
        if(this.data != null) {
            stack.push(this);
            record.push(false);
        }
        while(! stack.isEmpty()) {
            BST<Data> cursor = stack.pop();
            if(record.pop()) {
                cursor.size = cursor.quantity;
                if(cursor.above != null) {
                    cursor.size += cursor.above.size;
                }
                if(cursor.below != null) {
                    cursor.size += cursor.below.size;
                }
            } else {
                stack.push(cursor);
                record.push(true);
                if(cursor.above != null) {
                    stack.push(cursor.above);
                    record.push(false);
                }
                if(cursor.below != null) {
                    stack.push(cursor.below);
                    record.push(false);
                }
            }
        }
    }

    /**
     * Finds the number of Data objects stored in this BST
     * @return the size field
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * Checks for equality between this BST and another Object
     * @param o the comparator Object
     * @return true if this BST and the comparator Object are congruent, else false
     */
    @Override
    public boolean equals(Object o) {
        if(! (o instanceof BST<?> convert)) {
            return false;
        }
        if(this.size != convert.size) {
            return false;
        }
        Stack<BST<Data>> thisStack = new Stack<>();
        thisStack.push(this);
        Stack<BST<?>> oStack = new Stack<>();
        oStack.push(convert);
        Stack<Boolean> thisRecord = new Stack<>(), oRecord = new Stack<>();
        thisRecord.push(false);
        oRecord.push(false);
        LinkedQueue<BST<Data>> thisQueue = new LinkedQueue<BST<Data>>();
        LinkedQueue<BST<?>> oQueue = new LinkedQueue<BST<?>>();
        while(! thisStack.isEmpty()) {
            BST<Data> thisCursor = thisStack.pop();
            if(thisRecord.pop()) {
                thisQueue.add(thisCursor);
                if(thisCursor.above != null) {
                    thisStack.push(thisCursor.above);
                    thisRecord.push(false);
                }
            } else {
                thisStack.push(thisCursor);
                thisRecord.push(true);
                if(thisCursor.below != null) {
                    thisStack.push(thisCursor.below);
                    thisRecord.push(false);
                }
            }
            BST<?> oCursor = oStack.pop();
            if(oRecord.pop()) {
                oQueue.add(oCursor);
                if(oCursor.above != null) {
                    oStack.push(oCursor.above);
                    oRecord.push(false);
                }
            } else {
                oStack.push(oCursor);
                oRecord.push(true);
                if(oCursor.below != null) {
                    oStack.push(oCursor.below);
                    oRecord.push(false);
                }
            }
            while(! (thisQueue.isEmpty() || oQueue.isEmpty())) {
                BST<Data> thisNext = thisQueue.poll();
                BST<?> oNext = oQueue.poll();
                if(! (thisNext.data.equals(oNext.data) && thisNext.quantity == oNext.quantity)) {
                    return false;
                }
            }
        }
        return thisQueue.isEmpty() && oQueue.isEmpty();
    }

    /**
     * Provides the TrueText of this BST
     * @return this BST in a parsable format
     */
    @Override
    public String trueText() {
        Stack<BST<Data>> stack = new Stack<>();
        Stack<Boolean> record = new Stack<>();
        if(this.data != null) {
            stack.push(this);
            record.push(false);
        }
        StringBuilder builder = new StringBuilder();
        String delimiter = "";
        while(! stack.isEmpty()) {
            BST<Data> cursor = stack.pop();
            if(record.pop()) {
                builder.append(delimiter).append(cursor.data).append(":").append(cursor.quantity);
                delimiter = "|";
                if(cursor.above != null) {
                    stack.push(cursor.above);
                    record.push(false);
                }
            } else {
                stack.push(cursor);
                record.push(true);
                if(cursor.below != null) {
                    stack.push(cursor.below);
                    record.push(false);
                }
            }
        }
        return builder.toString();
    }

    /**
     * Converts this BST to a printable, inline format
     * @return this BST as a linear String
     */
    public String inlineText() {
        StringBuilder builder = new StringBuilder();
        Stack<BST<Data>> cursorStack = new Stack<>();
        cursorStack.push(this);
        Stack<Integer> layerStack = new Stack<>();
        layerStack.push(0);
        Stack<Boolean> record = new Stack<>();
        record.push(false);
        int prevLayer = -1;
        while(! cursorStack.isEmpty()) {
            BST<Data> cursor = cursorStack.pop();
            int layer = layerStack.pop();
            if(layer < prevLayer) {
                builder.append("]".repeat(prevLayer - layer));
            }
            if(record.pop()) {
                builder.append(cursor.data).append(":").append(cursor.quantity);
                if(cursor.above != null) {
                    cursorStack.push(cursor.above);
                    layerStack.push(layer + 1);
                    record.push(false);
                }
            } else {
                builder.append("[");
                cursorStack.push(cursor);
                layerStack.push(layer);
                record.push(true);
                if(cursor.below != null) {
                    cursorStack.push(cursor.below);
                    layerStack.push(layer + 1);
                    record.push(false);
                }
            }
            prevLayer = layer;
        }
        builder.append("]".repeat(prevLayer + 1));
        return builder.toString();
    }

    /**
     * Converts this BST to a printable format
     * @return this tree in String form
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Stack<BST<Data>> cursorStack = new Stack<>();
        cursorStack.push(this);
        Stack<Integer> layerStack = new Stack<>();
        layerStack.push(0);
        Stack<Boolean> record = new Stack<>();
        record.push(false);
        int maxLayer = 0;
        Map<Integer, String> indentMap = new HashMap<>();
        indentMap.put(maxLayer, "");
        while(! cursorStack.isEmpty()) {
            BST<Data> cursor = cursorStack.pop();
            int layer = layerStack.pop();
            if(record.pop()) {
                builder.append(indentMap.get(layer)).append(cursor.data).append(" [x").append(cursor.quantity).append("]\n");
                if(cursor.above != null) {
                    cursorStack.push(cursor.above);
                    layerStack.push(layer + 1);
                    record.push(false);
                }
            } else {
                if(layer > maxLayer) {
                    indentMap.put(layer, indentMap.get(layer - 1).concat("\t"));
                    maxLayer = layer;
                }
                cursorStack.push(cursor);
                layerStack.push(layer);
                record.push(true);
                if(cursor.below != null) {
                    cursorStack.push(cursor.below);
                    layerStack.push(layer + 1);
                    record.push(false);
                }
            }
        }
        return builder.toString();
    }

    /**
     * Prints this BST
     */
    public void print() {
        System.out.println(this);
    }
}