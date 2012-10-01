package com.alexshabanov.springrestapi.rest.common;

import com.alexshabanov.springrestapi.domain.DomainObject;

public final class IdHolder extends DomainObject {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static IdHolder as(int id) {
        final IdHolder holder = new IdHolder();
        holder.setId(id);
        return holder;
    }
}
