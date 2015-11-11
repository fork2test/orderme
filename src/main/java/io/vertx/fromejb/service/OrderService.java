package io.vertx.fromejb.service;

import io.vertx.core.Future;
import io.vertx.ext.web.RoutingContext;
import io.vertx.fromejb.common.AbstractService;
import io.vertx.fromejb.management.AppConstants;
import io.vertx.fromejb.model.Order;

/**
 * Created by fiorenzo on 11/11/15.
 */
public class OrderService extends AbstractService<Order>
{



   @Override
   public void stop(Future<Void> stopFuture) throws Exception
   {

   }

   @Override
   protected Order getObj(RoutingContext routingContext)
   {
      Order order = new Order();
      return order;
   }

   @Override
   public String getBasePath()
   {
      return AppConstants.APP_NAME + AppConstants.API_PATH + AppConstants.ORDERS_PATH;
   }

}
