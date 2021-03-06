package com.volmit.firefly;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Function;

public class KList<T> extends ArrayList<T> implements List<T>
{
	private static final long serialVersionUID = -2892550695744823337L;

	@SafeVarargs
	public KList(T... ts)
	{
		super();
		add(ts);
	}

	public KList()
	{
		super();
	}

	public KList(Collection<T> values)
	{
		super();
		add(values);
	}

	public KList(Enumeration<T> e)
	{
		super();
		add(e);
	}


	/**
	 * Remove the last element
	 */
	public KList<T> removeLast()
	{
		remove(last());

		return this;
	}


	private KList<T> add(Enumeration<T> e)
	{
		while(e.hasMoreElements())
		{
			add(e.nextElement());
		}

		return this;
	}

	public KList<T> add(Collection<T> values)
	{
		addAll(values);
		return this;
	}

	/**
	 * Cut this list into targetCount sublists
	 *
	 * @param targetCount
	 *            the target count of sublists
	 * @return the list of sublists
	 */
	public KList<KList<T>> divide(int targetCount)
	{
		return split(size() / targetCount);
	}

	/**
	 * Split this list into a list of sublists with roughly targetSize elements of T
	 * per sublist
	 *
	 * @param targetSize
	 *            the target size
	 * @return the list of sublists
	 */
	public KList<KList<T>> split(int targetSize)
	{
		targetSize = targetSize < 1 ? 1 : targetSize;
		KList<KList<T>> gg = new KList<>();
		KList<T> b = new KList<>();

		for(T i : this)
		{
			if(b.size() >= targetSize)
			{
				gg.add(b.copy());
				b.clear();
			}

			b.add(i);
		}

		if(!b.isEmpty())
		{
			gg.add(b);
		}

		return gg;
	}

	/**
	 * Rewrite this list by checking each value and changing the value (or not).
	 * Return null to remove the element in the function
	 *
	 * @param t
	 *            the function
	 * @return the same list (not a copy)
	 */
	public KList<T> rewrite(Function<T, T> t)
	{
		KList<T> m = copy();
		clear();

		for(T i : m)
		{
			addNonNull(t.apply(i));
		}

		return this;
	}

	/**
	 * To array
	 *
	 * @return the array
	 */
	@SuppressWarnings("unchecked")
	public T[] array()
	{
		return (T[]) toArray();
	}

	/**
	 * Return a copy of this list
	 *
	 * @return the copy
	 */
	public KList<T> copy()
	{
		return new KList<T>().add(this);
	}

	/**
	 * Shuffle the list
	 *
	 * @return the same list
	 */
	public KList<T> shuffle()
	{
		Collections.shuffle(this);
		return this;
	}

	/**
	 * Sort the list (based on toString comparison)
	 *
	 * @return the same list
	 */
	public KList<T> sort()
	{
		Collections.sort(this, (a, b) -> a.toString().compareTo(b.toString()));
		return this;
	}

	/**
	 * Reverse this list
	 *
	 * @return the same list
	 */
	public KList<T> reverse()
	{
		Collections.reverse(this);
		return this;
	}

	@Override
	public String toString()
	{
		return "[" + toString(", ") + "]";
	}

	/**
	 * Tostring with a seperator for each item in the list
	 *
	 * @param split
	 *            the seperator
	 * @return the string representing this object
	 */
	public String toString(String split)
	{
		if(isEmpty())
		{
			return "";
		}

		if(size() == 1)
		{
			return get(0).toString();
		}

		StringBuilder b = new StringBuilder();

		for(String i : toStringList())
		{
			b.append(split + i);
		}

		return b.toString().substring(split.length());
	}

	/**
	 * Invoke tostring on each value in the list into a string list
	 *
	 * @return the string list
	 */
	public KList<String> toStringList()
	{
		return convert((t) -> t.toString());
	}

	/**
	 * Convert a list into another list type. Such as GList<Integer> to
	 * GList<String>. list.convert((i) -> "" + i);
	 *
	 * @param <V>
	 * @param converter
	 * @return
	 */
	public <V> KList<V> convert(Function<T, V> converter)
	{
		KList<V> v = new KList<V>();
		forEach((t) -> v.addNonNull(converter.apply(t)));
		return v;
	}

	/**
	 * Adds T to the list, ignores if null
	 *
	 * @param t
	 *            the value to add
	 * @return the same list
	 */
	public KList<T> addNonNull(T t)
	{
		if(t != null)
		{
			super.add(t);
		}

		return this;
	}

	/**
	 * Swaps the values of index a and b. For example "hello", "world", "!" swap(1,
	 * 2) would change the list to "hello", "!", "world"
	 *
	 * @param a
	 *            the first index
	 * @param b
	 *            the second index
	 * @return the same list (builder), not a copy
	 */
	public KList<T> swapIndexes(int a, int b)
	{
		T aa = remove(a);
		T bb = get(b);
		add(a, bb);
		remove(b);
		add(b, aa);

		return this;
	}

	/**
	 * Remove a number of elements from the list
	 *
	 * @param t
	 *            the elements
	 * @return this list
	 */
	@SuppressWarnings("unchecked")
	public KList<T> remove(T... t)
	{
		for(T i : t)
		{
			super.remove(i);
		}

		return this;
	}

	/**
	 * Add another glist's contents to this one (addall builder)
	 *
	 * @param t
	 *            the list
	 * @return the same list
	 */
	public KList<T> add(KList<T> t)
	{
		super.addAll(t);
		return this;
	}

	/**
	 * Add a number of values to this list
	 *
	 * @param t
	 *            the list
	 * @return this list
	 */
	@SuppressWarnings("unchecked")
	public KList<T> add(T... t)
	{
		for(T i : t)
		{
			super.add(i);
		}

		return this;
	}

	/**
	 * Check if this list has an index at the given index
	 *
	 * @param index
	 *            the given index
	 * @return true if size > index
	 */
	public boolean hasIndex(int index)
	{
		return size() > index;
	}

	/**
	 * Get the last index of this list (size - 1)
	 *
	 * @return the last index of this list
	 */
	public int last()
	{
		return size() - 1;
	}

	/**
	 * Deduplicate this list by converting to linked hash set and back
	 *
	 * @return the deduplicated list
	 */
	public KList<T> dedupe()
	{
		return qclear().add(new LinkedHashSet<T>(this));
	}

	/**
	 * Clear this list (and return it)
	 *
	 * @return the same list
	 */
	public KList<T> qclear()
	{
		super.clear();
		return this;
	}

	/**
	 * Simply !isEmpty()
	 *
	 * @return true if this list has 1 or more element(s)
	 */
	public boolean hasElements()
	{
		return !isEmpty();
	}

	/**
	 * Pop the first item off this list and return it
	 *
	 * @return the popped off item or null if the list is empty
	 */
	public T pop()
	{
		if(isEmpty())
		{
			return null;
		}

		return remove(0);
	}

	/**
	 * Pop the last item off this list and return it
	 *
	 * @return the popped off item or null if the list is empty
	 */
	public T popLast()
	{
		if(isEmpty())
		{
			return null;
		}

		return remove(last());
	}

	public static KList<String> asStringList(List<?> oo)
	{
		KList<String> s = new KList<String>();

		for(Object i : oo)
		{
			s.add(i.toString());
		}

		return s;
	}

	@SuppressWarnings("unchecked")
	public KList<T> forceAdd(Object[] values)
	{
		for(Object i : values)
		{
			add((T) i);
		}

		return this;
	}

	@SuppressWarnings("unchecked")
	public KList<T> forceAdd(int[] values)
	{
		for(Object i : values)
		{
			add((T) i);
		}

		return this;
	}

	@SuppressWarnings("unchecked")
	public KList<T> forceAdd(double[] values)
	{
		for(Object i : values)
		{
			add((T) i);
		}

		return this;
	}

	@SuppressWarnings("unchecked")
	public KList<T> forceAdd(float[] values)
	{
		for(Object i : values)
		{
			add((T) i);
		}

		return this;
	}

	@SuppressWarnings("unchecked")
	public KList<T> forceAdd(byte[] values)
	{
		for(Object i : values)
		{
			add((T) i);
		}

		return this;
	}

	@SuppressWarnings("unchecked")
	public KList<T> forceAdd(short[] values)
	{
		for(Object i : values)
		{
			add((T) i);
		}

		return this;
	}

	@SuppressWarnings("unchecked")
	public KList<T> forceAdd(long[] values)
	{
		for(Object i : values)
		{
			add((T) i);
		}

		return this;
	}

	@SuppressWarnings("unchecked")
	public KList<T> forceAdd(boolean[] values)
	{
		for(Object i : values)
		{
			add((T) i);
		}

		return this;
	}

	public T middleValue()
	{
		return get(middleIndex());
	}

	private int middleIndex()
	{
		return size() % 2 == 0 ? (size() / 2) : ((size() / 2) + 1);
	}


	public KList<T> qdel(T t)
	{
		remove(t);
		return this;
	}
}
