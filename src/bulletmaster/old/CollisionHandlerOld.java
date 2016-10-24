package bulletmaster.old;

import bulletmaster.collisions.CollisionListener;
import bulletmaster.entity.Entity;
import bulletmaster.entity.Hero;

//http://accu.org/index.php/journals/496
//http://stackoverflow.com/questions/3644065/how-to-write-an-elegant-collision-handling-mechanism
public class CollisionHandlerOld{
	  public final static EmptyListener
      EMPTY_LISTENER = new EmptyListener();
  private CollisionMapDispatcher dispatcher
     = CollisionMapDispatcher.getInstance();
  public static void heroAndHornWorm( 
  Entity hero, Entity hornworm)   {
	  //Change states appropriately
	  //   Hero p =(Entity)Player;
   // p.playerDied();
   // p.destroy();
    //alien.destroyed();
  }
 
  public static void registerAllCollisions(){
	  dispatcher.addCollisionListener(
        Hero.class, Hornworm.class,
        new CollisionListener() {
          public void collisionDetected( 
                     Entity ent11,
                   Entity ent2) {
            heroAndHornWorm( ent1, ent2);
          }
        } );
  ...
// More collisions registered here
  }
// ---------------------------
// I N N E R  C L A S S E S
// ---------------------------
  public static class EmptyListener 
  implements CollisionListener {
    public void collisionDetected( 
       WorldObject obj1, WorldObject obj2) {
// Do nothing
    }
  }
}
