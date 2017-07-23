import scalafx.scene.image.{Image,ImageView};
import scalafx.scene.shape.{Path, MoveTo, LineTo};

/**
 * @author WongYiJin
 */
class OctopusMonster(_x:Double,_y:Double,speed:Double) extends Monster(_x,_y,speed){
  
  private val octopusMonsterImg = new Image("images/octopus.png");
  private val octopusMonster = new ImageView(octopusMonsterImg)
  {
    x = _x;
    y = _y;
    fitHeight = 65;
    fitWidth = 65;
  }
  
  val path = new Path()
  {
    elements = Array(
        MoveTo(x,y),
        LineTo(x+50,y+50),
        LineTo(x,y+150),
        LineTo(x-50,y+300),
        LineTo(x,y+400),
        LineTo(x-50,y+450),
        LineTo(x,y+550));
  }
  
  children = octopusMonster;
}