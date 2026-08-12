package org.swiggy.enums;

public enum OrderStatus {
    PLACED,
    ACCEPTED_BY_RESTAURANT,
    PREPARING,
    READY_FOR_PICKUP,
    OUT_FOR_DELIVERY,
    DELIVERED,
    CANCELLED;

    public boolean canTransitionTo(OrderStatus nextStatus){
        return switch(this) {
            case PLACED -> nextStatus == ACCEPTED_BY_RESTAURANT || nextStatus == CANCELLED;
            case ACCEPTED_BY_RESTAURANT -> nextStatus == PREPARING
                    || nextStatus == CANCELLED;

            case PREPARING -> nextStatus == READY_FOR_PICKUP;

            case READY_FOR_PICKUP -> nextStatus == OUT_FOR_DELIVERY;

            case OUT_FOR_DELIVERY -> nextStatus == DELIVERED;

            case DELIVERED, CANCELLED -> false;

        };
    }
}
