package domain.memento;

import java.util.HashSet;
import java.util.Set;

import domain.Reservation;

public class MementoResource {
	private final Set<Reservation> reservations;
	private final Set<Reservation> previousReservations;
	
	public Set<Reservation> getReservations()
	{
		return new HashSet<>(this.reservations);
	}
	
	public Set<Reservation> getPreviousReservations()
	{
		return new HashSet<>(this.previousReservations);
	}
	
	public MementoResource(Set<Reservation> reservations, Set<Reservation> previousReservations)
	{
		this.reservations = new HashSet<>(reservations);
		this.previousReservations = new HashSet<>(previousReservations);
	}
}
