package bulletmaster.old;

import java.util.Hashtable;

import bulletmaster.collisions.CollisionListener;
import bulletmaster.entity.Entity;

public class CollisionMapDispatcher {
	private final static String SEPARATOR="<+>"; 
	private Hashtable collisionTable;

	private static CollisionMapDispatcher dispatcher = new CollisionMapDispatcher();

	private CollisionMapDispatcher() {
		collisionTable = new Hashtable();
	}

	public static CollisionMapDispatcher getInstance(){
		return(dispatcher);
	}

	public void addCollisionListener( 
			Class class1, 
			Class class2, 
			CollisionListener listener){
		// FIXME: should check the classes are
		// WorldObject type.
		String s1 = class1.getName();
		String s2 = class2.getName();
		CollisionTuple tuple = 
				new CollisionTuple( s1, s2, listener);
		collisionTable.put( s1 + SEPARATOR + s2, tuple);
	}

	public void genericCollisionEvent(Entity ent1,
			Entity ent2) {
		String s1 = ent1.getClass().getName();
		String s2 = ent2.getClass().getName();
		CollisionTuple tuple = 
				(CollisionTuple)collisionTable.get(
						s1 + SEPARATOR + s2);
		if(tuple != null) {
			// Call the collision listener object
			tuple.listener.collisionDetected(ent1,
					ent2);
		}
		else {
			// Reverse the order
			tuple =(CollisionTuple)collisionTable.get( 
					s2 + SEPARATOR + s1);
			if(tuple != null) {
				// Call the collision listener object
				tuple.listener.collisionDetected(ent2,
						ent1);
			}
			else {
				// throw new InternalError("cannot handle 
				// collision between classes `"+ s1+"' and 
				// `"+s2+"'");
				return;
			}
		}
	}
	// ---------------------------
	// I N N E R  C L A S S E S
	// ---------------------------
	private static class CollisionTuple {
		String entityClass1;
		String entityClass2;
		CollisionListener listener;

		public CollisionTuple(String class1,
				String class2, 
				CollisionListener listener) {
			this.entityClass1 = class1;
			this.entityClass2 = class2;
			this.listener = listener;
		}
	}
}
