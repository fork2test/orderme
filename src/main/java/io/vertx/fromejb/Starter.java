package io.vertx.fromejb;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.fromejb.management.AppConstants;
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
         JDBCClient jdbcClient = JDBCClient.createShared(vertx, new JsonObject()
                  .put("url", url)
                  .put("driver_class", "com.mysql.jdbc.Driver")
                  .put("user", username)
                  .put("password", password));
         Router router = Router.router(vertx);

         router.route().handler(BodyHandler.create());

         // in order to minimize the nesting of call backs we can put the JDBC connection on the context for all routes
         // that match /products
         // this should really be encapsulated in a reusable JDBC handler that uses can just add to their app
         router.route(AppConstants.APP_NAME + "*").handler(routingContext -> jdbcClient.getConnection(res -> {
            if (res.failed())
            {
               routingContext.fail(res.cause());
            }
            else
            {
               SQLConnection sqlConnection = res.result();

               // save the connection on the context
               routingContext.put("sqlConnection", sqlConnection);

               // we need to return the connection back to the jdbc pool. In order to do that we need to close it, to keep
               // the remaining code readable one can add a headers end handler to close the connection.
               routingContext.addHeadersEndHandler(done -> sqlConnection.close(v -> {
               }));

               routingContext.next();
            }
         })).failureHandler(routingContext -> {
            SQLConnection conn = routingContext.get("conn");
            if (conn != null)
            {
               conn.close(v -> {
               });
            }
         });
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
