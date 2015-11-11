package io.vertx.fromejb.service;

import io.vertx.ext.web.RoutingContext;
import io.vertx.fromejb.common.AbstractService;
import io.vertx.fromejb.management.AppConstants;
import io.vertx.fromejb.model.Pizza;

/**
 * Created by fiorenzo on 11/11/15.
 */
public class PizzaService extends AbstractService<Pizza>
{

   @Override protected Pizza getObj(RoutingContext routingContext)
   {
      Pizza pizza = new Pizza();
      return pizza;
   }

   @Override public String getBasePath()
   {
      return AppConstants.APP_NAME + AppConstants.API_PATH + AppConstants.PIZZA_PATH;
   }
}
