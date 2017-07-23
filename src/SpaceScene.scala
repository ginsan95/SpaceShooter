import scalafx.scene.{Scene,Group};
import scalafx.scene.paint.Color;
import scalafx.Includes._;
import scalafx.scene.input.{KeyEvent,KeyCode}
import scalafx.scene.shape.Rectangle;
import scalafx.animation.{KeyFrame, Timeline}
import scalafx.event.ActionEvent;
import scala.util.Random;
import scalafx.util.Duration;
import scala.collection.mutable.ArrayBuffer;
import scalafx.scene.Node;
import scalafx.scene.text._;
import scalafx.scene.media.{Media,MediaPlayer};
import scalafx.scene.image.{Image,ImageView};
import java.io.File;
import scalafx.application.Platform;
import javafx.{ concurrent => jfxc }
import scalafx.concurrent.Task;

/**
 * @author AveryChoke
 */
object SpaceScene {
  
  private val rand:Random = new Random();
  private val monsters = ArrayBuffer[Monster]();
  private val bullets = ArrayBuffer[Bullet]();
  private var player:SpaceShip = null;
  private var hp:Double = 0;
  private var _score = 0;
  private var end:Boolean = false;
  private var canShoot:Boolean = false;
  private var monsterSpeed:Double = 0;
  private var monsterAmount = 0;
  
  //Get and set
  def score = _score;
  def score_= (s:Int) {_score = s}
  
  private val background = new ImageView(new Image("images/universe.jpeg"));
  
  //Create walls to stop the player from going out of window
  private val topWall = Rectangle(0,0,760,0);
  private val rightWall = Rectangle(760,0,0,500);
  private val leftWall = Rectangle(0,0,0,510);
  private val bottomWall = Rectangle(0,500,760,0);
  
  //Create text to display the current HP and score
  private val text = new Text(10,30,"");
  text.font = new Font(20);
  text.fill = Color.Red;
  
  //The Group of nodes for SpaceScene
  private val space = new Group { }
  
  //Method to initialize all the values for the user to replay the game
  private def initialize(speed:Double, amount:Int)
  {
    monsterSpeed = speed;
    monsterAmount = amount;
    hp = 10;
    score = 0;
    blinkCount = 0;
    end = false;  //Reset to false so that threads can run
    canShoot = false; //Reset to false in case the player is still shooting when it dies
    
    player = new SpaceShip(400,400);
    
    //Clear all of them to completely restart the game and boost performance
    space.children.clear();
    monsters.clear();
    bullets.clear();
    
    //Add in all the initial required nodes into the children
    space.children += (background,topWall, rightWall, leftWall, bottomWall, player, text);
  }
  
  //Method to start the threads
  //The threads are in class instead of object because killed threads can
  //never be restarted again. Therefore create another new thread can restart the threads
  private def startThreads()
  {
    new Thread(new createMonsterThread).start();
    new Thread(new playerThread).start()
    new Thread(new shootBulletThread).start();
    new Thread(new removeDestroyedNodeThread).start();
  }
  
  //Thread to create monsters
  private class createMonsterThread extends Task(new jfxc.Task[Unit] {
    protected def call(): Unit = {
      while(!end && !GameApplication.shutDown)
      {
        Platform.runLater {
          for(i<-1 to monsterAmount)
          {
            //Subtype Polymorphism apply here.
            //Although it is possible to call the moving() method of each object but if more monsters
            //objects were to be created then it will be tedious.
            val newMonsters:Array[Monster] = Array(
                new OctopusMonster(rand.nextInt(600)+50, 20, monsterSpeed),
                new TentacleMonster(rand.nextInt(600)+50, 20, monsterSpeed));
            for(moveMonster:Monster <- newMonsters)
            {
              moveMonster.moving();
              monsters += moveMonster;
              space.children += moveMonster;
            }
          }
        }
        Thread.sleep(1500);
      }
    }
  })
  
  //Thread to handle the player.
  //Handle if the player hit something, bullet hit monster, collide with walls
  private class playerThread extends Task(new jfxc.Task[Unit] {
    protected def call(): Unit = {
      while(!end && !GameApplication.shutDown)
      {
        Platform.runLater {
          damage();
          destroy();
          wallCollision();        
          text.text = ("HP : " + hp + "\nScore : " + score);
          if(hp <= 0)
          {
            end = true;  //Boolean to kill the the threads
            GameApplication.stage.scene = GameApplication.createEndScene();
          }
        }
        Thread.sleep(10);
      }
    }
  })
  
  //Thread to shoot the bullets
  private class shootBulletThread extends Task(new jfxc.Task[Unit] {
    protected def call(): Unit = {
      while(!end && !GameApplication.shutDown)
      {
        if(canShoot)
        {
          Platform.runLater {
            //Play sound
            //val pew = new Media(new File("src/sounds/pew.mp3").toURI.toString());
            val pew = new Media(getClass().getResource("sounds/pew.mp3").toString());
            val pewPlayer = new MediaPlayer(pew);
            pewPlayer.volume = 0.3;
            pewPlayer.play();
            
            val bul = new Bullet(player.x+25,player.y-20);
            //Make bullet follow the movement of player
            bul.translateX = player.moveX;
            bul.translateY = player.moveY;
            bul.moving();
            bullets += bul;
            space.children += bul;
          }
        }
        Thread.sleep(200);
      }
    }
  })
  
  //Thread to remove the destroyed bullet and monster from the children to prevent overflow
  //And also to remove the destroyed bullet and monster from their respective array
  //to reduce loop count and improve performance
  private class removeDestroyedNodeThread extends Task(new jfxc.Task[Unit] {
    protected def call(): Unit = {
      while(!end && !GameApplication.shutDown)
      {
        Platform.runLater {
          //Filter the monsters and bullets which are already useless
          val removedMonsters = monsters.filter { x => x.visible.value == false }
          val removedBullets = bullets.filter { x => x.visible.value == false }
          
          //Remove the filtered monsters and bullets
          monsters --= removedMonsters;
          bullets --= removedBullets;
          removedMonsters.foreach { x => space.children -= x }
          removedBullets.foreach { x => space.children -= x }
        }
        Thread.sleep(5000);
      }
    }
  })
  
  //Function to check the damage to player by monster
  private def damage()
  {
    for(i<-monsters)
    {
      if(collide(player,i) && i.visible.value==true)
      {
        val dish = new Media(getClass().getResource("sounds/dish.mp3").toString());
        val dishPlayer = new MediaPlayer(dish);
        dishPlayer.play();
        blink.play();
        i.visible= false;
        hp -= 1;
      }
      else if(collide(i,bottomWall) && i.visible.value==true)
      {
        val boom = new Media(getClass().getResource("sounds/boom.mp3").toString());
        val boomPlayer = new MediaPlayer(boom);
        boomPlayer.play();
        i.visible = false;
        hp -= 0.5;
      }
    }
  }
  
  //Function to kill the monsters
  private def destroy()
  {
    for(i<-bullets)
    {
      for(j<-monsters)
      {
        if(collide(i,j) && j.visible.value==true)
        {
          i.visible = false;
          j.visible = false;
          score += 1;
        }
      }
      if(collide(i,topWall) && i.visible.value==true)
      {
        i.visible = false;
      }
    }
  }
  
  //A KeyFrame that moves the player smoothly
  private val movePlayerFrame = KeyFrame(15 ms, onFinished = {
    event: ActionEvent =>
      player.moving();
  })
  private val movePlayer = new Timeline {
    keyFrames = Seq(movePlayerFrame);
    cycleCount = Timeline.Indefinite;
  }
  
  //A KeyFrame to blink the player to indicate it is attacked
  private var blinkCount = 0;
  private val blinkFrame = KeyFrame(50 ms, onFinished = {
        event: ActionEvent =>
          if(blinkCount%2==0)
          {
            player.visible = false;
          }
          else
          {
            player.visible = true;
          }
          blinkCount += 1;
  })
  private val blink = new Timeline {
    keyFrames = Seq(blinkFrame);
    cycleCount = 8;
  }
  
  //Check player collision with walls
  private def wallCollision() =
  { 
    if(collide(player,topWall))
    {
      movePlayer.stop();
      player.moveVerti() = player.moveVerti.value + 10;
    }
    else if (collide(player,bottomWall))
    {
      movePlayer.stop();
      player.moveVerti() = player.moveVerti.value - 10;
    }
    else if (collide(player,leftWall))
    {
      movePlayer.stop();
      player.moveHori() = player.moveHori.value + 10;
    }
    else if (collide(player,rightWall))
    {
      movePlayer.stop();
      player.moveHori() = player.moveHori.value - 10;
    }
    else
      movePlayer.play();
  }
  
  //Generic method to check the collision between 2 objects
  private def collide[A <: Node, B <: Node](nodeA:A, nodeB:B): Boolean =
  {
    nodeA.boundsInParent().intersects(nodeB.boundsInParent());
  }
  
  //Start the space Game
  def startGame(speed:Double, amount:Int)
  {
    //Initialize the values
    initialize(speed, amount);
    
    //Start threads
    startThreads();
  }
  
  //The Game Scene for the game
  val spaceScene:Scene = new Scene(750,500)
  {
     onKeyPressed = (k: KeyEvent) => k.code match {
        case KeyCode.RIGHT =>
          {
            player.directionHori = 5;
          }
        case KeyCode.LEFT =>
          {
            player.directionHori = -5;
          }
        case KeyCode.UP =>
          {
            player.directionVerti = -5;
          }
        case KeyCode.DOWN =>
          {
            player.directionVerti = 5;
          }
        case KeyCode.Z =>
        {
          canShoot = true;
        }
        case _ =>
     }
     
     onKeyReleased = (k: KeyEvent) => k.code match {
        case KeyCode.RIGHT =>
          {
            player.directionHori = 0;
          }
        case KeyCode.LEFT =>
          {
            player.directionHori = 0;
          }
        case KeyCode.UP =>
          {
            player.directionVerti = 0;
          }
        case KeyCode.DOWN =>
          {
            player.directionVerti = 0;
          }
        case KeyCode.Z =>
        {
          canShoot = false;
        }
        case _ =>
     }
    root = space;
  }
}