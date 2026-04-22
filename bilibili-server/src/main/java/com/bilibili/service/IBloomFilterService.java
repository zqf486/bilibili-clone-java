package com.bilibili.service;

import org.redisson.api.RBloomFilter;

import java.util.Collection;
import java.util.Map;

/**
 * 布隆过滤器服务接口
 * 提供布隆过滤器的创建、查询和操作功能
 */
public interface IBloomFilterService {

    /**
     * 获取或创建布隆过滤器
     * 如果过滤器不存在，则使用指定参数创建；如果已存在，则返回现有实例
     *
     * @param filterName         过滤器名称
     * @param expectedInsertions 预计插入数量
     * @param falseProbability   误判率
     * @param <T>                元素类型
     * @return 布隆过滤器实例
     */
    <T> RBloomFilter<T> getFilter(String filterName, long expectedInsertions, double falseProbability);

    /**
     * 检查元素是否可能存在于布隆过滤器中
     *
     * @param filterName 过滤器名称
     * @param value      要检查的元素
     * @param <T>        元素类型
     * @return true如果元素可能存在，false如果元素一定不存在
     */
    <T> boolean mightContain(String filterName, T value);

    /**
     * 检查元素是否可能存在于布隆过滤器中（使用默认配置）
     * 如果过滤器不存在，将使用默认参数创建
     *
     * @param filterName 过滤器名称
     * @param value      要检查的元素
     * @param <T>        元素类型
     * @return true如果元素可能存在，false如果元素一定不存在
     */
    <T> boolean mightContainWithDefault(String filterName, T value);

    /**
     * 添加元素到布隆过滤器
     *
     * @param filterName 过滤器名称
     * @param value      要添加的元素
     * @param <T>        元素类型
     * @return true如果添加成功，false如果添加失败或元素已存在
     */
    <T> boolean add(String filterName, T value);

    /**
     * 添加元素到布隆过滤器（使用默认配置）
     * 如果过滤器不存在，将使用默认参数创建
     *
     * @param filterName 过滤器名称
     * @param value      要添加的元素
     * @param <T>        元素类型
     * @return true如果添加成功，false如果添加失败或元素已存在
     */
    <T> boolean addWithDefault(String filterName, T value);

    /**
     * 批量添加元素到布隆过滤器
     *
     * @param filterName 过滤器名称
     * @param values     要添加的元素集合
     * @param <T>        元素类型
     * @return 成功添加的元素数量
     */
    <T> int addAll(String filterName, Collection<T> values);

    /**
     * 批量添加元素到布隆过滤器（使用默认配置）
     * 如果过滤器不存在，将使用默认参数创建
     *
     * @param filterName 过滤器名称
     * @param values     要添加的元素集合
     * @param <T>        元素类型
     * @return 成功添加的元素数量
     */
    <T> int addAllWithDefault(String filterName, Collection<T> values);

    /**
     * 初始化布隆过滤器（如果不存在则创建）
     *
     * @param filterName         过滤器名称
     * @param expectedInsertions 预计插入数量
     * @param falseProbability   误判率
     * @return true如果初始化成功或过滤器已存在，false如果初始化失败
     */
    boolean initFilter(String filterName, long expectedInsertions, double falseProbability);

    /**
     * 检查布隆过滤器是否存在
     *
     * @param filterName 过滤器名称
     * @return true如果过滤器存在，false如果不存在
     */
    boolean exists(String filterName);

    /**
     * 删除布隆过滤器
     *
     * @param filterName 过滤器名称
     * @return true如果删除成功，false如果删除失败或过滤器不存在
     */
    boolean delete(String filterName);

    /**
     * 清空布隆过滤器中的所有元素
     * 注意：清空后过滤器仍然存在，可以继续使用
     *
     * @param filterName 过滤器名称
     * @return true如果清空成功，false如果清空失败或过滤器不存在
     */
    boolean clear(String filterName);

    /**
     * 获取布隆过滤器的预计插入数量
     *
     * @param filterName 过滤器名称
     * @return 预计插入数量，如果过滤器不存在返回-1
     */
    long getExpectedInsertions(String filterName);

    /**
     * 获取布隆过滤器的误判率
     *
     * @param filterName 过滤器名称
     * @return 误判率，如果过滤器不存在返回-1.0
     */
    double getFalseProbability(String filterName);

    /**
     * 获取布隆过滤器中已添加的元素数量估计值
     *
     * @param filterName 过滤器名称
     * @return 已添加元素数量的估计值，如果过滤器不存在返回-1
     */
    long getEstimatedSize(String filterName);
}