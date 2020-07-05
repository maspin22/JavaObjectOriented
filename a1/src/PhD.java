
public class PhD {
	/** NetId: mjs698, mia27. Time spent: hh hours, mm minutes.
	 	An instance maintains info about the PhD of a person. */
	
	private String name; // Name of the person with a PhD, String of length > 1.
	private int year; // Year PhD awarded, can be any number.
	private int month; //  Month PhD awarded, in range 1..12, 1 being January...
	private PhD advisor1; /* The first PhD advisor of this person 
						     null if unknown.*/
	private PhD advisor2; /* The second advisor of this person null if unknown 
						     or if the person has less than two advisors. */
	private int numAdvisees; // Number of PhD advisees of this person.
	
	/** Constructor: an instance for a person with name n, 
	 *  PhD year y, and PhD month m.
	 *  The advisors are unknown, and there are 0 advisees.
	 *  Precondition: n has at least 2 chars, and m is in 1..12. */
	public PhD(String n, int y, int m)	
	{
		assert n != null && n.length() >= 2 && m >= 1 && m <= 12;
		name = n;
		year = y;
		month = m;
		numAdvisees = 0;
	}
	
	/** Return the name of this person. */
	public String name()
	{
		return name;
	}
	
	/** Return the year this person got their PhD. */
	public int year() 
	{
		return year;
	}
	
	/** Return the month this person got their PhD. */
	public int month() 
	{
		return month;
	}
	
	/** Return the first advisor of this PhD (null if unknown). */
	public PhD advisor1() 
	{
		return advisor1;
	}
	
	/** Return the second advisor of this PhD (null if unknown or 
	 *  non-existent). */
	public PhD advisor2() 
	{
		return advisor2;
	}
	
	/** Return the number of PhD advisees of this person. */
	public int numAdvisees() 
	{
		return numAdvisees;
	}
	
	/** Add p as the first advisor of this person.
	 *  Precondition: the first advisor is unknown and p is not null. */
	public void addAdvisor1(PhD p)
	{
		assert advisor1 == null && p != null;
		advisor1 = p;
		p.numAdvisees += 1;
	}
	
	/** Add p as the second advisor of this PhD.
	 *  Precondition: The first advisor is known, the second advisor is unknown, 
	 *  p is not null, and p is different from the first advisor. */
	public void addAdvisor2(PhD p)
	{
		assert advisor1 != null && advisor2 == null && 
			   p != null && p != advisor1;
		advisor2 = p;
		p.numAdvisees += 1;
	}
	/** Constructor: a PhD with name n, PhD year y, PhD month m, 
	 *  first advisor adv1, and no second advisor.
	 *  Precondition: n has at least 2 chars, m is in 1..12, 
	 *  and adv1 is not null. */
	public PhD(String n, int y, int m, PhD adv1)
	{
		this(n, y, m);
		assert adv1 != null;
		advisor1 = adv1;
		adv1.numAdvisees += 1;
	}
	
	/** Constructor: a PhD with name n, PhD year y, PhD month m, first advisor
	 *  adv1, and second advisor adv2.
	 *  Precondition: n has at least 2 chars, m is in 1..12,
	 *  adv1 and adv2 are not null, and adv1 and adv2 are different. */
	public PhD(String n, int y, int m, PhD adv1, PhD adv2)
	{
		this(n, y, m, adv1);
		advisor2 = adv2;
		adv2.numAdvisees += 1;
		
	}
	/** Return value of: p is not null and this PhD got the PhD before p. */
	public boolean gotBefore(PhD p)
	{
		return year < p.year || (year == p.year && month < p.month);
	}
	
	/** Return value of: this PhD is an intellectual sibling of p. 
	 *  Precondition: p is not null. */
	public boolean isSiblingOf(PhD p)
	{
		assert p != null;
		
		boolean hasOneAdvisor = advisor1 != null && p.advisor1 != null;
		boolean hasTwoAdvisors = advisor2 != null && p.advisor2 != null;
		
		return this != p && hasOneAdvisor && (advisor1 == p.advisor1 || 
			   advisor1 == p.advisor2 || advisor2 == p.advisor1 ||
			   (hasTwoAdvisors && advisor2 == p.advisor2));
	}
	
	
}
