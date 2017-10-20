package com.lsh.atp.core.dao;

/**
 * Project Name: lsh-risk
 * Created by fuhao
 * Date: 16/8/24
 * Time: 16/8/24.
 * 北京链商电子商务有限公司
 * Package name:com.lsh.risk.core.dao.
 * desc:类功能描述
 */

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Intercepts({
        @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
        @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class }) })
public class SqlStatementInterceptor implements Interceptor{

    private static Logger logger = LoggerFactory.getLogger(SqlStatementInterceptor.class);

    private final ExecutorService executor = Executors.newSingleThreadExecutor();


    public Object intercept(Invocation invocation) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            Object ret = invocation.proceed();
            this.executor.submit(new LogTask(invocation, start, System.currentTimeMillis()));
            return ret;
        } catch (Throwable t) {
            this.executor.submit(new LogTask(invocation, start, System.currentTimeMillis(), t));
            throw t;
        }
    }


    public Object plugin(Object obj) {
        return Plugin.wrap(obj, this);
    }


    public void setProperties(Properties properties) {
    }

    private static class LogTask implements Callable<Boolean> {

        private static final String SEPARATOR = "\1\t";

        private final Invocation invocation;

        private final long startTime;

        private final long endTime;

        private final Throwable throwable;

        public LogTask(final Invocation invocation, final long startTime, final long endTime) {
            this(invocation, startTime, endTime, null);
        }

        public LogTask(final Invocation invocation, final long startTime, final long endTime, final Throwable throwable) {
            this.invocation = invocation;
            this.startTime = startTime;
            this.endTime = endTime;
            this.throwable = throwable;
        }



        public Boolean call() throws Exception {
            Object[] args = this.invocation.getArgs();
            if (ArrayUtils.isEmpty(args)) {
                return false;
            }
            Object arg0 = args[0];
            if (arg0 == null || !(arg0 instanceof MappedStatement)) {
                return false;
            }
            MappedStatement mappedStatement = (MappedStatement) arg0;
            Object parameter = null;
            if (args.length > 1) {
                parameter = args[1];
            }

            String sqlId = mappedStatement.getId();
            BoundSql boundSql = mappedStatement.getBoundSql(parameter);
            Configuration configuration = mappedStatement.getConfiguration();

            String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
            List<Object> params = this.getParams(configuration, boundSql);
            StringBuilder builder = new StringBuilder("SQL日志==>[").append(sqlId).append("]");
            builder.append(SEPARATOR).append("SQL：").append(sql);
            //拼接参数
            builder.append(SEPARATOR).append("参数：");
            if (!CollectionUtils.isEmpty(params)) {
                int countor = params.size();
                for (Object param : params) {
                    if (param == null) {
                        builder.append("null");
                    } else {
                        if (param instanceof Date) {
                            builder.append(DateFormat.getDateTimeInstance().format((Date) param));
                        } else {
                            builder.append(param.toString());
                        }
                        builder.append("(").append(param.getClass().getSimpleName()).append(")");
                    }
                    if ((--countor) > 0) {
                        builder.append(", ");
                    }
                }
            }
            builder.append(SEPARATOR).append("时间：").append(new Timestamp(this.startTime).toString());
            builder.append(SEPARATOR).append("耗时：").append(this.endTime - this.startTime).append("ms");
            if (this.throwable == null) {
                SqlStatementInterceptor.logger.info(builder.toString());
            } else {
                SqlStatementInterceptor.logger.error(builder.toString(), this.throwable);
            }
            return true;
        }

        private List<Object> getParams(Configuration configuration, BoundSql boundSql) {
            Object parameterObject = boundSql.getParameterObject();
            if (parameterObject != null) {
                TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
                if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                    return Collections.singletonList(parameterObject);
                }
            }
            List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
            if (!CollectionUtils.isEmpty(parameterMappings)) {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                List<Object> list = new ArrayList<Object>(parameterMappings.size());
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        list.add(metaObject.getValue(propertyName));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        list.add(boundSql.getAdditionalParameter(propertyName));
                    }
                }
                return list;
            }
            return Collections.emptyList();
        }
    }

}