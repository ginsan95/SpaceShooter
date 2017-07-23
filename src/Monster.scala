import scalafx.scene.Group;
import scalafx.util.Duration;
import scalafx.animation.PathTransition;
import scalafx.scene.shape.Path;

/**
 * @author WongYiJin
 */
abstract class Monster(private var _x:Double, private var _y:Double, private val speed:Double)
extends Group with Movable
{
  def x:Double = _x;
  def y:Double = _y;
  
  def x_= (ux:Double)
  {
    _x = ux;
  }
  
  def y_= (uy:Double)
  {
    _y = uy;
  }
  
  val path:Path;
  private lazy val fall = new PathTransition(Duration(speed),path,this);
  
  def moving()
  {
    fall.play();
  }
}