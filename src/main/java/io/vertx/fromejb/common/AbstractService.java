package io.vertx.fromejb.common;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/**
 * Created by fiorenzo on 11/11/15.
 */
public abstract class AbstractService<T> extends AbstractVerticle implements RsService
{
   Logger logger = LoggerFactory.getLogger(this.getClass());

   Router router;
   Repository<T> repository;

   public AbstractService()
   {
   }

   public AbstractService(Repository<T> repository)
   {
      this.repository = repository;
   }

   public void addRoutes()
   {
      logger.info("@ADD ROUTES PATH");
      // post create
      getRouter().post(getBasePath()).handler(this::persist);

      //get single
      getRouter().get(getBasePath() + ":id").handler(this::fetch);

      // put update
      getRouter().put(getBasePath() + ":id").handler(this::update);

      //delete delete
      getRouter().delete(getBasePath() + ":id").handler(this::delete);

      //get exist
      getRouter().get(getBasePath() + ":id").handler(this::exist);

      //get list
      getRouter().get(getBasePath()).handler(this::getList);

      //options
      getRouter().options(getBasePath()).handler(this::options);
   }

   public void start(Future<Void> startFuture) throws Exception
   {
      logger.info("@START");
      addRoutes();
   }

   public void stop(Future<Void> stopFuture) throws Exception
   {
      logger.info("@STOP");

   }

   protected abstract T getObj(RoutingContext routingContext);

   public abstract String getBasePath();

   public Router getRouter()
   {
      return router;
   }

   public void setRouter(Router router)
   {
      this.router = router;
   }

   public Repository<T> getRepository()
   {
      return repository;
   }

   public void setRepository(Repository<T> repository)
   {
      this.repository = repository;
   }

   //   @POST
   //   T object
   public void persist(RoutingContext routingContext)
   {
      logger.info("@POST");
      T t = getObj(routingContext);
      getRepository().persist(t, result -> {
         if (result.failed())
         {
            routingContext.fail(result.cause());
            return;
         }
         routingContext.response().end(result.result().toString());
      });
   }

   //   @GET
   //   @Path("/{id}")
   //   @PathParam("id") String id
   public void fetch(RoutingContext routingContext)
   {
      logger.info("@GET");
      String id = routingContext.get("id");
      getRepository().find(id, result -> {
         if (result.failed())
         {
            routingContext.fail(result.cause());
            return;
         }
         routingContext.response().end(result.result().toString());
      });
   }

   //   @PUT
   //   @Path("/{id}")
   //   @PathParam("id") String id
   //   T object
   public void update(RoutingContext routingContext)
   {
      logger.info("@PUT");
      T t = getObj(routingContext);
      getRepository().update(t, result -> {
         if (result.failed())
         {
            routingContext.fail(result.cause());
            return;
         }
         routingContext.response().end(result.result().toString());
      });
   }

   //   @DELETE
   //   @Path("/{id}")
   //   @PathParam("id") String id
   public void delete(RoutingContext routingContext)
   {
      logger.info("@DELETE");
      String id = routingContext.get("id");
      getRepository().delete(id, result -> {
         if (result.failed())
         {
            routingContext.fail(result.cause());
            return;
         }
         routingContext.response().end(result.result().toString());
      });
   }

   //   @GET
   //   @Path("/{id}/exist")
   //   @PathParam("id") String id

   public void exist(RoutingContext routingContext)
   {
      logger.info("@GET EXIST");
      String id = routingContext.get("id");
      getRepository().exist(id, result -> {
         if (result.failed())
         {
            routingContext.fail(result.cause());
            return;
         }
         routingContext.response().end(result.result().toString());
      });
   }

   //   @GET
   //   @DefaultValue("0") @QueryParam("startRow") Integer startRow,
   //   @DefaultValue("10") @QueryParam("pageSize") Integer pageSize,
   //   @QueryParam("orderBy") String orderBy,
   //   @Context UriInfo ui
   public void getList(RoutingContext routingContext)
   {
      logger.info("@GET LIST");
      String startRow = routingContext.get("startRow");
      String pageSize = routingContext.get("pageSize");
      String orderBy = routingContext.get("orderBy");
      JsonObject search = routingContext.getBodyAsJson();
      getRepository().getList(search,
               Integer.valueOf(startRow != null ? startRow : "0"),
               Integer.valueOf(pageSize != null ? pageSize : "0"), result -> {

               });
   }

   //   @OPTIONS
   public void options(RoutingContext routingContext)
   {
      logger.info("@OPTIONS");
   }

   //   @OPTIONS
   //   @Path("{path:.*}")
   public void allOptions(RoutingContext routingContext)
   {
      logger.info("@OPTIONS ALL");
      routingContext.response().end("");
   }

}
