package io.vertx.fromejb.common;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;

import java.util.List;

/**
 * Created by fiorenzo on 11/11/15.
 */
public interface Repository<T>
{

   public void getList(SQLConnection conn, JsonObject search, int startRow, int pageSize,
            Handler<AsyncResult<List<T>>> handler);

   public void getListSize(SQLConnection conn, JsonObject search, Handler<AsyncResult<Integer>> handler);

   public void find(SQLConnection conn, Object key, Handler<AsyncResult<T>> handler);

   public void fetch(SQLConnection conn, Object key, Handler<AsyncResult<T>> handler);

   public void persist(SQLConnection conn, T object, Handler<AsyncResult<T>> handler);

   public void update(SQLConnection conn, T object, Handler<AsyncResult<T>> handler);

   public void delete(SQLConnection conn, Object key, Handler<AsyncResult<Boolean>> handler);

   public void exist(SQLConnection conn, Object key, Handler<AsyncResult<Boolean>> handler);

}
