package io.vertx.fromejb;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.web.Router;
import io.vertx.fromejb.service.OrderService;
import io.vertx.fromejb.service.PizzaService;

/**
 * Created by fiorenzo on 11/11/15.
 */
public class Starter extends AbstractVerticle
{
   String ip;
   String dbHost;
   String dbPort;
   String appName;
   String username;
   String password;
   Logger logger = LoggerFactory.getLogger(Starter.class.getName());

   public void start(Future<Void> startFuture) throws Exception
   {
      Router router = Router.router(vertx);
      useOpenShift();
      initDb();
      OrderService orderService = new OrderService();
      PizzaService pizzaService = new PizzaService();
      vertx.deployVerticle(orderService);
      vertx.deployVerticle(pizzaService);
      logger.info("start");
   }

   private void useOpenShift()
   {
      int port = Integer.parseInt(System.getenv("OPENSHIFT_VERTX_PORT"));
      String ip = System.getenv("OPENSHIFT_VERTX_IP");
      String dbHost = System.getenv("OPENSHIFT_MYSQL_DB_HOST");
      String dbPort = System.getenv("OPENSHIFT_MYSQL_DB_PORT");
      String appName = System.getenv("OPENSHIFT_APP_NAME");
      String username = System.getenv("OPENSHIFT_MYSQL_DB_USERNAME");
      String password = System.getenv("OPENSHIFT_MYSQL_DB_PASSWORD");
   }

   private void initDb()
   {

      String url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + appName;
      logger.info("url: " + url + ", user:" + username + ", " + password);
      try
      {
         JDBCClient client = JDBCClient.createShared(vertx, new JsonObject()
                  .put("url", url)
                  .put("driver_class", "com.mysql.jdbc.Driver")
                  .put("user", username)
                  .put("password", password));
         getVertx().getOrCreateContext().put("JDBCClient", client);
      }
      catch (Throwable e)
      {
         logger.info(e.getMessage());
      }
   }

   @Override
   public void stop(Future<Void> stopFuture) throws Exception
   {
      logger.info("stop");
   }
}
