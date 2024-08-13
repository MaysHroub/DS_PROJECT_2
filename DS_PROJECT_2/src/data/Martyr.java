package data;


public class Martyr implements Comparable<Martyr> {
	
	private String name;
	private int age;
	private char gender;

	public Martyr() { }
	
	public Martyr(String name, int age, char gender) {
		setName(name);
		setAge(age);
		setGender(gender);
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		if (age < 0) throw new IllegalArgumentException("Invalid age value");
		this.age = age;
	}

	public char getGender() {
		return gender;
	}

	public void setGender(char gender) {
		if (gender != 'F' && gender != 'M') 
			throw new IllegalArgumentException("We don't allow other genders here hhhh :)");
		this.gender = gender;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Martyr)) return false;
		Martyr m = (Martyr) obj;
		return name.equalsIgnoreCase(m.name) && age == m.age; // check the name for the combo box selection
	}

	@Override
	public int compareTo(Martyr o) {
		return (age == o.age) ? gender - o.gender : age - o.age;
	}
	

}








