package com.jhc.o2o.dao.split;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Locale;
import java.util.Properties;

@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class DynamicDataSourceInterceptor implements Interceptor {
    private static final String REGEX = ".*insert\\u0020.*|.*delete\\u0020.*|.*update\\u0020.*";
    private static Logger logger = LoggerFactory.getLogger(DynamicDataSourceInterceptor.class);


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //决定用哪一个DataSource
        String lookupKey = DynamicDataSourceHolder.DB_MASTER;
        boolean synchronizationActive = TransactionSynchronizationManager.isActualTransactionActive();
        //获取参数
        Object[] objects = invocation.getArgs();
        MappedStatement ms = (MappedStatement) objects[0];//头部为操作
        //判断是否为事务
        if (!synchronizationActive) {
            //读方法
            if (ms.getSqlCommandType().equals(SqlCommandType.SELECT)) {
                //selectKey为自增id查询主键(select last_insert_id())方法，使用主库
                if (ms.getId().contains(SelectKeyGenerator.SELECT_KEY_SUFFIX)) {
                    lookupKey = DynamicDataSourceHolder.DB_MASTER;
                } else {
                    BoundSql boundSql = ms.getSqlSource().getBoundSql(objects[1]);
                    String sql = boundSql.getSql().toLowerCase(Locale.CHINA).replaceAll("[\\t\\n\\r]", " ");
                    if (sql.matches(REGEX)) {
                        lookupKey = DynamicDataSourceHolder.DB_MASTER;
                    } else {
                        lookupKey = DynamicDataSourceHolder.DB_SLAVE;
                    }
                }
            }
        }else {//事务
            lookupKey = DynamicDataSourceHolder.DB_MASTER;
        }
        logger.debug("设置方法[{}]use[{}]Strategy,SqlCommandType[{}]..",ms.getId(),lookupKey,ms.getSqlCommandType().name());
        DynamicDataSourceHolder.setDbType(lookupKey);
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {//如果是Executor类型（增删改查），则使用intercept拦截
            return Plugin.wrap(target, this);
        } else {//否则不拦截，返回本体
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
