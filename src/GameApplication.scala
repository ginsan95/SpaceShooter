import scalafx.application.JFXApp;
import scalafx.application.JFXApp.PrimaryStage;
import scalafx.scene.Scene;
import scalafx.Includes._;
import scalafxml.core.{FXMLLoader, NoDependencyResolver}

/**
 * @author VannessTan
 */
object GameApplication extends JFXApp{
  
  //Boolean to kill threads
  var shutDown:Boolean = false;
  
  val mainMenuScene = new Scene(750,500)
  {
    val resource = getClass.getResource("MainMenu.fxml");
    val loader = new FXMLLoader( resource, NoDependencyResolver)
    loader.load();
    root = loader.getRoot[javafx.scene.layout.AnchorPane];
  }
  
  val howToPlayScene = new Scene(750,500)
  {
    val resource = getClass.getResource("HowToPlay.fxml");
    val loader = new FXMLLoader( resource, NoDependencyResolver)
    loader.load();
    root = loader.getRoot[javafx.scene.layout.AnchorPane];
  }
  
  //Create the new endScene each time as each endScene has different score
  def createEndScene():Scene =
  {
    val endScene = new Scene(750,500)
    {
      val resource = getClass.getResource("EndScene.fxml");
      val loader = new FXMLLoader( resource, NoDependencyResolver)
      loader.load();
      root = loader.getRoot[javafx.scene.layout.AnchorPane];
    }
    endScene;
  }

  stage = new PrimaryStage
  {
    title = "P4 Space Shooter"
    resizable = false;
    scene = mainMenuScene;
  }
  
  //Kill all the threads if the user suddenly close the window
  stage.onCloseRequest() = handle
  {
    shutDown = true;
  }
}