package com.gtech.abj;

/** OSGi bundle service interface. */
public interface InstantiateDealer {

/**
 * Creates a {@link DealerActor} on a new {@link akka.actor.ActorSystem}.
 * 
 * @param actorSystemName       name of the new {@link akka.actor.ActorSystem}
 * @param dealerID              name of the new {@link DealerActor}
 * 
 * @return {@code true} if the creation completed successfully
 */
boolean instantiate(String actorSystemName, String dealerID);
}