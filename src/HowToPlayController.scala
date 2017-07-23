import scalafxml.core.macros.sfxml
import scalafx.scene.control.Button
import scalafx.event.ActionEvent
import scalafx.scene.text._;

/**
 * @author VannessTan
 */
@sfxml
class HowToPlayController(private val returnButton:Button) {
  
  def returnButtonClick()
  {
    GameApplication.stage.scene = GameApplication.mainMenuScene;
  } 
  
}