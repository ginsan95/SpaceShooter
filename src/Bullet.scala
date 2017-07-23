import scalafx.scene.Node
import scalafx.animation.TranslateTransition
import scalafx.util.Duration
import scalafx.scene.Group
import scalafx.scene.shape.Rectangle;
import scalafx.scene.paint.Color;

/**
 * @author DanielChin
 */
class Bullet(private var _x:Double, private var _y:Double) extends Group with Movable{
  
  def x:Double = _x;
  def y:Double = _y;
  
  def x_= (ux:Double)
  {
    _x = ux;
    bullet.x = ux;
  }
  
  def y_= (uy:Double)
  {
    _y = uy;
    bullet.y = y;
  }
  
  private val bullet = Rectangle(x,y,10,20);
  bullet.fill = Color.Yellow;
  
  private val shootUp = new TranslateTransition(Duration(1100),bullet)
  {
    toY = -600
  }
    
  def moving()
  {
    shootUp.play()
  }
  
  children = bullet;
}