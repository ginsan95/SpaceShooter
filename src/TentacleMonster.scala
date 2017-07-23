import scalafx.scene.image.{Image,ImageView};
import scalafx.scene.shape.{Path, MoveTo, LineTo};

/**
 * @author WongYiJin
 */
class TentacleMonster (_x:Double,_y:Double,speed:Double) extends Monster(_x,_y,speed){
  
  private val tentacleMonsterImg = new Image("images/tentacle.png");
  private val tentacleMonster = new ImageView(tentacleMonsterImg)
  {
    x = _x;
    y = _y;
    fitHeight = 70;
    fitWidth = 70;
  }
  
  val path = new Path()
  {
    elements = Array(
        MoveTo(x,y),
        LineTo(x,y+50),
        LineTo(x-50,y+150),
        LineTo(x+100,y+250),
        LineTo(x-100,y+350),
        LineTo(x+100,y+450),
        LineTo(x,y+550));
  }
  
  children = tentacleMonster;
}