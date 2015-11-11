package io.vertx.fromejb.common;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * Created by fiorenzo on 11/11/15.
 */
public interface Repository<T>
{

   public void getList(JsonObject search, int startRow, int pageSize, Handler<AsyncResult<List<T>>> handler);

   public void getListSize(JsonObject search, Handler<AsyncResult<Integer>> handler);

   public void find(Object key, Handler<AsyncResult<T>> handler);

   public void fetch(Object key, Handler<AsyncResult<T>> handler);

   public void persist(T object, Handler<AsyncResult<T>> handler);

   public void update(T object, Handler<AsyncResult<T>> handler);

   public void delete(Object key, Handler<AsyncResult<Boolean>> handler);

   public void exist(Object key, Handler<AsyncResult<Boolean>> handler);

}
