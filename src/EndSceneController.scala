import scalafxml.core.macros.sfxml
import scalafx.scene.control.{Button,Label}
import scalafx.event.ActionEvent
import scalafx.scene.text._;

/**
 * @author AveryChoke
 */
@sfxml
class EndSceneController(
    private val backMainMenuButton:Button,
    private val scoreLabel:Label) {
  

  scoreLabel.text = ("Score : " + SpaceScene.score);
  
  def backMainMenuClick(event: ActionEvent) = 
  {
    GameApplication.stage.scene = GameApplication.mainMenuScene;
  }
  
}