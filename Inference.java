package homework;

/**
 *
 * @author nitishareddy
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.List;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Set;

public class homework {
	
	private static Scanner in;
	private static PrintWriter out;	
	
	public static void main(String[] args) throws FileNotFoundException {
		
		
		File inputFile = new File("input.txt");
		in = new Scanner(inputFile);
		out = new PrintWriter("output.txt");
		
		LinkedList<LinkedList<Fol>> KnowledgeBase = new LinkedList<>();
		Hashtable<String, Set<Integer>> pointer = new Hashtable<>(); // <Predicate, idInKnowledgeBase>, predicate could be ~
		LinkedList<Fol> queryList = new LinkedList<>();
		
		//System.out.println("Queries:");
		int noOfQueries = in.nextInt();
		in.nextLine();
		for (int i = 0; i < noOfQueries; i++) {
			
			String queryStr = in.nextLine();
			//System.out.println(queryStr);
			
			Fol p = toPred(queryStr, 0);
			queryList.add(p);
		}
		
		int variable = 0;
		
		//System.out.println("KnowledgeBase:");
		int noOfSentences = in.nextInt();
		in.nextLine();
		for (int i = 0; i < noOfSentences; i++) {
			
			String str = in.nextLine();
			//System.out.println(str);
			// str is one KnowledgeBase sentence
			String[] sentences = str.split("\\&"); // sentences sharing the same variable
			for (String sentence : sentences) {
				LinkedList<Fol> clause = new LinkedList<>();
				String[] predicates = sentence.split("\\|");
				for (String predicate : predicates) {
					Fol p = toPred(predicate, variable);
					String key = p.literal;
					if (!p.positive) {
						key = "~" + key;
					}
					if (!pointer.containsKey(key)) {
						pointer.put(key, new HashSet<Integer>());
					}
					pointer.get(key).add(KnowledgeBase.size());
					clause.add(p);
				}
				KnowledgeBase.add(clause);
			}
			variable++;
		}
		// now I got the KnowledgeBase
		
		for (int i = 0; i < queryList.size(); i++) {
			
			Fol p = queryList.get(i);
			p.positive = !p.positive; // negate query
			
			LinkedList<Fol> negatedQueryClause = new LinkedList<>();
			negatedQueryClause.add(p);
			

			if (resolution(new LinkedList<>(KnowledgeBase), copyPointer(pointer), negatedQueryClause)) {
				out.println("TRUE");
			//	System.out.println("TRUE");
			} else {
				out.println("FALSE");
			//	System.out.println("FALSE");
			}
		}

		out.close();
	}
	
	
	
	// string to Predicate
	private static Fol toPred(String s, int variable) {
		
		boolean positive = true;
		int id = 0;
		while (!Character.isLetter(s.charAt(id))) {
			if (s.charAt(id++) == '~') 
                            positive = false;
		}
		int literalStart = id;
		while (s.charAt(id) != '(') {
			id++;
		}
		int pStart = id;
		String literal = s.substring(literalStart, pStart);
		
		while (s.charAt(id) != ')') {
			id++;
		}
		String argStr = s.substring(pStart+1, id).replaceAll(" ", "");
		String[] params = argStr.split(",");
		LinkedList<String> parameters = new LinkedList<>();
		for (String param : params) {
			if (isVar(param)) 
                            param = param + variable;
			parameters.add(param);
		}
		return new Fol(literal, parameters, positive);
	}
        
        // PRE: s.length() > 0
	private static boolean isVar(String s) {
		return Character.isLowerCase(s.charAt(0));
	}
	
	// PRE: used theta to update var before...
	// add clause to KnowledgeBase, and update the pointer... what if it is already in the KnowledgeBase???
        
        private static boolean resolution(LinkedList<LinkedList<Fol>> KnowledgeBase, Hashtable<String, Set<Integer>> pointer, LinkedList<Fol> clause) {
		
		long startTime = System.currentTimeMillis();		
		
		LinkedList<LinkedList<Fol>> queryList = new LinkedList<>();
		queryList.add(clause);
		
		while (!queryList.isEmpty()) {
			
			LinkedList<LinkedList<Fol>> result = new LinkedList<>();
			
			for (LinkedList<Fol> query : queryList) {
				
				for (int i = 0; i < query.size(); i++) { // each Predicate in query
					Fol p = query.get(i);
					String negKey = p.literal;
					if (p.positive) {
						negKey = "~" + negKey;
					}
					
					// add time...
					if ((System.currentTimeMillis() - startTime)/1000 > 10) {
						return false;
					}
					
					if (pointer.containsKey(negKey)) {
						Set<Integer> idInKnowledgeBase = pointer.get(negKey);
						for (int id : idInKnowledgeBase) {
							LinkedList<Fol> newList = KnowledgeBase.get(id);
							for (int j = 0; j < newList.size(); j++) { // each Predicate in pal
								
								Hashtable<String, String> theta = new Hashtable<>(); // ?..I guess...
								if ((theta = resolve(p, newList.get(j), theta)) != null) { // It is a pal!
									
									LinkedList<Fol> newClause = new LinkedList<>();// clause exclude i, pal exclude j
									
									LinkedList<Fol> queryCopy = copyClause(query);
									queryCopy.remove(i);
									updateVariable(queryCopy, theta);
									newClause.addAll(queryCopy);
									
									// shadow reference...
									LinkedList<Fol> listCopy = copyClause(newList);
									listCopy.remove(j);
									updateVariable(listCopy, theta);
									newClause.addAll(listCopy);
									
									if (newClause.size() == 0) {
										return true;
									}							
									result.add(newClause);
								}
							}
						}						
					}
				}
			}
			
			
			boolean someNew = false;
			for (int i = 0; i < queryList.size(); i++) {
				LinkedList<Fol> query = queryList.get(i);
				// add query to KnowledgeBase and update pointer
				if(addToKnowledgeBase(KnowledgeBase, pointer, query)) {
					someNew = true;
				}
				// add time...
				if ((System.currentTimeMillis() - startTime)/1000 > 10) {
					return false;
				}
			}
			if (!someNew) return false;
			
			queryList = result;
		}
		
		return false;
	}
        
        private static Hashtable<String, String> resolve(Fol a, Fol b, Hashtable<String, String> theta) {
		if (theta == null) return null;
		if ((!a.literal.equals(b.literal)) || (a.positive == b.positive)) return null;
		return substituteList(a.parameters, b.parameters, theta);
	}
	
	private static Hashtable<String, String> substitute(Fol a, Fol b, Hashtable<String, String> theta) {
		if (theta == null) return null;
		 if ((!a.literal.equals(b.literal)) || (a.positive != b.positive)) return null;
		return substituteList(a.parameters, b.parameters, theta);
	}
        
        private static Hashtable<String, String> substituteList(List<String> list1, List<String> list2, Hashtable<String, String> theta) {
		if (theta == null) return null;
		if (list1.size() != list2.size()) return null;
		
		String s1 = list1.get(0);
		String s2 = list2.get(0);

		if (list1.size() == 1 && list2.size() == 1) {
			return substituteVariable(s1, s2, theta);
		}
		return substituteList(list1.subList(1, list1.size()), list2.subList(1, list2.size()), substituteVariable(s1, s2, theta));
	}
	
	// PRE: theta is updated...
	private static Hashtable<String, String> substituteVariable(String s1, String s2, Hashtable<String, String> theta) {
		if (theta == null) return null;
		
		if (theta.containsKey(s1)) {
			s1 = theta.get(s1);
		} else if (theta.containsKey(s2)) {
			s2 = theta.get(s2);
		} // s2 is ground (not key)
		
		if (!isVar(s1) && !isVar(s2)) { // maybe switch to Constant before...
			if (!s1.equals(s2)) {
				return null;
			}
			return theta;
		}
		
		if (!isVar(s1)) {
			String tmp = s1;
			s1 = s2;
			s2 = tmp;
		} // s1 is var
		
		theta.put(s1, s2);
		return theta;
	}	
        
        private static void updateVariable(LinkedList<Fol> clause, Hashtable<String, String> theta) {
		
		for (Fol p : clause) {
			LinkedList<String> parameters = p.parameters;
			for (int i = 0; i < parameters.size(); i++) {
				
				String key = parameters.get(i);
				if (theta.containsKey(key)) {
					parameters.set(i, theta.get(key));
				}
//				while (theta.containsKey(key)) {
//					key = theta.get(key);
//				}
//				parameters.set(i, key);
			}
		}
	}

	
	
	
	private static LinkedList<Fol> copyClause(LinkedList<Fol> clause) {
		LinkedList<Fol> copy = new LinkedList<>();
		for (int i = 0; i < clause.size(); i++) {
			Fol pCopy = new Fol(clause.get(i));	
			copy.add(pCopy);
		}
		return copy;
	}
	
	
	
	private static boolean addToKnowledgeBase(LinkedList<LinkedList<Fol>> KnowledgeBase, Hashtable<String, Set<Integer>> pointer, LinkedList<Fol> clause) {
		
		// check if already in KnowledgeBase
		for (LinkedList<Fol> clauseInKnowledgeBase : KnowledgeBase) {
			if (clauseInKnowledgeBase.toString().equals(clause.toString())) return false;
		}
		
		for (Fol p : clause) {
			String key = p.literal;
			if (!p.positive) {
				key = "~" + key;
			}
			if (!pointer.containsKey(key)) {
				pointer.put(key, new HashSet<Integer>());
			}
			pointer.get(key).add(KnowledgeBase.size());
		}
		KnowledgeBase.add(clause);
		return true;
	}
	
	private static Hashtable<String, Set<Integer>> copyPointer(Hashtable<String, Set<Integer>> map) {
		
		Hashtable<String, Set<Integer>> mapCopy = new Hashtable<>();
                map.entrySet().forEach((entry) -> {
                    Set<Integer> value = new HashSet<>();
                    for (int i : entry.getValue()) {
                        value.add(i);
                    }
                    mapCopy.put(entry.getKey(), value);
            });
		return mapCopy;
	}
	
	// PRE: KnowledgeBase and pointer are copies
	
}

class Fol {
	String literal;
	LinkedList<String> parameters;
	boolean positive;
	public Fol(String literal, LinkedList<String> parameters, boolean positive) {
		this.literal = literal;
		this.parameters = parameters;
		this.positive = positive;
	}
	
	public Fol(Fol p) {
		literal = p.literal;
		parameters = new LinkedList<>(p.parameters);
		positive = p.positive;
	}
	
    public String toString() {
    	String res = literal + parameters;
    	if(!positive) {
    		res = "~" + res;
    	}
    	return res;
     } 	
}
