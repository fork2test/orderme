package io.vertx.fromejb.common;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * Created by fiorenzo on 11/11/15.
 */
public interface Repository<T>
{

   public void getList(JsonObject search, int startRow, int pageSize, Handler<List<T>> handler);

   public void getListSize(JsonObject search, Handler<Integer> handler);

   public void find(Object key, Handler<T> handler);

   public void fetch(Object key, Handler<T> handler);

   public void persist(T object, Handler<T> handler);

   public void update(T object, Handler<T> handler);

   public void delete(Object key, Handler<Boolean> handler);

   public void exist(Object key, Handler<Boolean> handler);

}
