package prover.utility.utilities;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import prover.structure.regular.entity.Entity;
import prover.structure.regular.entity.element.Element;
import prover.structure.regular.entity.proposition.Proposition;
import prover.utility.Utility;


public abstract class NewCollection extends Utility {
	
	public static <T, E extends Entity<E>> T chooseEntityType(Class<E> klass, T propChoice, T elementChoice) {
		if(Proposition.class.isAssignableFrom(klass)) return propChoice;
		else if(Element.class.isAssignableFrom(klass)) return elementChoice;
		else throw new AssertionError();
	}
	

	
	
	public static <S, T> Pair<S, T> pair(S left, T right){
		return new Pair<S, T>(left, right);
	}
	
	
	
	
	public static <T> List<T> list(){
		return new ArrayList<T>();
	}

	public static <T> List<T> list(T object){
		List<T> list = list();
		list.add(object);
		return list;
	}

	public static <T> List<T> list(T object1, T object2){
		List<T> list = list();
		list.add(object1);
		list.add(object2);
		return list;
	}
	
	public static <T> List<T> list(T object1, T object2, T object3){
		List<T> list = list();
		list.add(object1);
		list.add(object2);
		list.add(object3);
		return list;
	}

	public static <T> List<T> list(Collection<? extends T> list){
		return new ArrayList<T>(list);
	}
	
	public static <T> List<T> list(Collection<? extends T> collection, T element){
		List<T> list = list(collection);
		list.add(element);
		return list;
	}
	
	public static <T> List<T> list(Collection<? extends T> collection, T element1, T element2){
		List<T> list = list(collection);
		list.add(element1);
		list.add(element2);
		return list;
	}
	
	public static <T> List<T> list(T element, Collection<? extends T> collection){
		List<T> list = list(element);
		list.addAll(collection);
		return list;
	}
	
	public static <T> List<T> list(Collection<? extends T> collection1, Collection<? extends T> collection2){
		List<T> list = list(collection1);
		list.addAll(collection2);
		return list;
	}
	
	public static <T> List<T> list(Collection<? extends T> collection1, Collection<? extends T> collection2, Collection<? extends T> collection3){
		List<T> list = list(collection1);
		list.addAll(collection2);
		list.addAll(collection3);
		return list;
	}

	
	
	
	public static <T> Set<T> set(Collection<? extends T> collection){
		return new HashSet<T>(collection);
	}
	
	public static <T> Set<T> set(){
		return new HashSet<T>();
	}

	public static <T> Set<T> set(T object){
		Set<T> set = set();
		set.add(object);
		return set;
	}

	public static <T> Set<T> set(T object1, T object2){
		Set<T> set = set();
		set.add(object1);
		set.add(object2);
		return set;
	}

	public static <T> Set<T> set(Collection<? extends T> collection, T element){
		Set<T> set = set(collection);
		set.add(element);
		return set;
	}
	
	public static <T> Set<T> set(Collection<? extends T> collection, T element1, T element2){
		Set<T> set = set(collection);
		set.add(element1);
		set.add(element2);
		return set;
	}
	
	public static <T> Set<T> set(T element, Collection<? extends T> collection){
		return set(collection, element);
	}
	
	public static <T> Set<T> set(Collection<? extends T> collection1, Collection<? extends T> collection2){
		Set<T> set = set(collection1);
		set.addAll(collection2);
		return set;
	}
	
	
	
	public static <S, T> Map<S, T> map(Map<? extends S, ? extends T> map){
		return new HashMap<S, T>(map);
	}

	public static <S, T> Map<S, T> map(){
		return new HashMap<S, T>();
	}

	public static <S, T> Map<S, T> map(S key, T value){
		Map<S, T> map = map();
		map.put(key, value);
		return map;
	}

	public static <S, T> Map<S, T> map(List<? extends S> keys, List<? extends T> values){
		Map<S, T> map = map();
		for(int i=0; i<keys.size(); i++) {
			map.put(keys.get(i), values.get(i));
		}
		return map;
	}
	
	public static <T> Queue<T> queue() {
		return new ArrayDeque<T>();
	}
	
	
	
	
	public static <T> Set<List<T>> factorialize(List<Set<T>> listOfSets){
		Set<List<T>> ret = set();
		ret.add(list());
		
		for(Set<T> set : listOfSets) {
			Set<List<T>> newRet = set();
			for(T t : set) {
				for(List<T> list : ret) {
					newRet.add(list(list, t));
				}
			}
			ret = newRet;
		}
		
		return ret;		
	}
	
	public static <T> Set<T> intersect(Set<T> a, Set<T> b){
		if(a == null) return b;
		else if(b == null) return a;
		else {
			Set<T> c = set(a);
			c.retainAll(b);
			return c;
		}
	}

	public static <T> Set<Set<T>> product(Set<Set<T>> a, Set<Set<T>> b) {
		if(a == null) return b;
		else if(b == null) return a;
		else {
			Set<Set<T>> c = set();
			for(Set<T> d : a) {
				for(Set<T> e : b) {
					c.add(set(d, e));
				}
			}
			return c;
		}
	}

	public static <T> Set<T> nullToEmpty(Set<T> set) {
		if(set == null) return NewCollection.set();
		else return set;
	}

}