package endpoints

import java.io.File
import java.time.LocalDate

import akka.actor.ActorSystem
import akka.stream.scaladsl.Sink
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import akka.util.ByteString
import javax.inject.Inject
import play.api.libs.ws.WSClient
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import utils.LocalDateUtil

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future

class PullData  @Inject()(ws: WSClient, controllerComponent: ControllerComponents) extends AbstractController(controllerComponent) {

  implicit val actorSystem = ActorSystem("bse")
  implicit val materializer = ActorMaterializer(ActorMaterializerSettings(actorSystem).withInputBuffer(initialSize = 32, maxSize = 32))

  def getDataByDate(from: String, to: String): Action[AnyContent] = Action.async {
    implicit request =>
        val fromDate = LocalDate.parse(from)
        val toDate = LocalDate.parse(to)

        LocalDateUtil.rangeInclusive(fromDate, toDate).map(date => {
          val stringDate = LocalDateUtil.formatDDMMYY(date)
          val bseRequest = ws.url(s"https://www.bseindia.com/download/BhavCopy/Equity/EQ${stringDate}_CSV.ZIP")
          val responseF = bseRequest
            .addHttpHeaders("Content-type" -> "application/x-zip-compressed")
            .withMethod("GET").stream()

          responseF.flatMap(res => {
            val directoryName = s"/Users/ul58jb/PC/personal/code/Stock/data/raw/bse/equity/${date.getYear}/${date.getMonth}"
            val directory = new File(directoryName)
            if (!directory.exists()) {
              println("=============="+directory.mkdirs())
            }
            val file = new File(directoryName + s"/EQ${stringDate}_CSV.ZIP")
            val outputStream = java.nio.file.Files.newOutputStream(file.toPath)

            // The sink that writes to the output stream
            val sink = Sink.foreach[ByteString] { bytes =>
              outputStream.write(bytes.toArray)
            }

            // materialize and run the stream
            res.bodyAsSource
              .runWith(sink)
              .andThen {
                case _ =>
                  // Close the output stream whether there was an error or not
                  outputStream.close()
              }
        })
      })
      Future.successful(Ok("Success"))
  }
}
