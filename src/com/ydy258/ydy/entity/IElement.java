package com.ydy258.ydy.entity;



/**
 *  
 *  访问�?(Visitor)模式的接�?, 参�?�http://www.javaeye.com/topic/207092
 */

public interface IElement {
    public abstract void accept(IVisitor ivisitor);
}
