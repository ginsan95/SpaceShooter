import scalafx.scene.Group;
import scalafx.scene.image.{Image,ImageView};
import scalafx.beans.property.DoubleProperty;
import scalafx.scene.effect.BlendMode;

/**
 * @author DanielChin
 */
class SpaceShip(private var _x:Double, private var _y:Double) extends Group with Movable
{
  private var _moveHori = new DoubleProperty;
  private var _moveVerti = new DoubleProperty;
  private var _directionHori:Double = 0;
  private var _directionVerti:Double = 0;
  
  //Get Set start
  def x:Double = _x;
  def y:Double = _y;
  
  def x_= (ux:Double)
  {
    _x = ux;
    player.x = ux;
  }
  
  def y_= (uy:Double)
  {
    _y = uy;
    player.y = uy;
  }
  
  def moveHori = _moveHori;
  def moveVerti = _moveVerti;
  
  def moveHori_= (hori:DoubleProperty) {_moveHori = hori}
  def moveVerti_= (verti:DoubleProperty) {_moveVerti = verti}
  
  def directionHori = _directionHori;
  def directionVerti = _directionVerti;
  
  def directionHori_= (hori:Double) {_directionHori = hori}
  def directionVerti_= (verti:Double) {_directionVerti = verti}
  //Get Set end
  
  def moveX:Double =
  {
    player.translateX.value;
  }
  
  def moveY:Double =
  {
    player.translateY.value;
  }
  
  private val playerImg = new Image("images/ship.png");
  private val player = new ImageView(playerImg)
  {
    x = _x;
    y = _y;
    fitHeight = 70;
    fitWidth = 60;
    translateX <== moveHori;
    translateY <== moveVerti;
    //blendMode = BlendMode.Darken
  }
  
  def moving()
  {
    moveHori() = moveHori.value + directionHori;
    moveVerti() = moveVerti.value + directionVerti;
  }
  
  children = player;
}