import scalafxml.core.macros.sfxml
import scalafx.scene.control.Button
import scalafx.event.ActionEvent

/**
 * @author VannessTan
 */
@sfxml
class MainMenuController(
    private val easyButton:Button,
    private val normalButton:Button,
    private val hardButton:Button,
    private val howButton:Button) {
  
  def easyClick (event: ActionEvent) = 
  {
    GameApplication.stage.scene = SpaceScene.spaceScene;
    SpaceScene.startGame(5000,1);
  }
  
  def normalClick (event: ActionEvent) = 
  {
    GameApplication.stage.scene = SpaceScene.spaceScene;
    SpaceScene.startGame(4000,2);
  }
  
  def hardClick (event: ActionEvent) = 
  {
    GameApplication.stage.scene = SpaceScene.spaceScene;
    SpaceScene.startGame(3000,3);
  }
  
  def howClick (event: ActionEvent) = 
  {
    GameApplication.stage.scene = GameApplication.howToPlayScene;
  }
}