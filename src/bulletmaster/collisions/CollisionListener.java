package bulletmaster.collisions;

import bulletmaster.entity.Entity;

public interface CollisionListener {
	  public void collideWith( 
			    Entity ent1, 
			    Entity ent2);
	  
}
