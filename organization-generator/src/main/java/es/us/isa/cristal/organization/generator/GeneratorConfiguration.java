package es.us.isa.cristal.organization.generator;

import es.us.isa.cristal.organization.generator.distributors.Distributor;
import es.us.isa.cristal.organization.generator.functions.Function;
import es.us.isa.cristal.organization.model.gson.Person;
import es.us.isa.cristal.organization.model.gson.Position;
import es.us.isa.cristal.organization.model.gson.Role;
import es.us.isa.cristal.organization.model.gson.Unit;

/**
 * 
 * @author Manuel Leon
 *
 */
public class GeneratorConfiguration {
	
		private Function numberOfPersonsFunction;
		
		private Function numberOfRolesFunction;
		
		private Function numberOfPositionsFunction;
		
		private Function numberOfUnitsFunction;
		
		private Distributor<Position,Unit> positionUnitDistributor;
		
		private Distributor<Person,Position> personPositionDistributor;
		
		private Distributor<Role,Position> rolePositionDistributor;
		
		private Distributor<Position,Position> positionReportsDistributor;
		
		private Distributor<Position,Position> positionDelegatesDistributor;

		
		
		
		

		public final Function getNumberOfPersonsFunction() {
			return numberOfPersonsFunction;
		}

		public final void setNumberOfPersonsFunction(Function numberOfPersonsFunction) {
			this.numberOfPersonsFunction = numberOfPersonsFunction;
		}

		public final Function getNumberOfRolesFunction() {
			return numberOfRolesFunction;
		}

		public final void setNumberOfRolesFunction(Function numberOfRolesFunction) {
			this.numberOfRolesFunction = numberOfRolesFunction;
		}

		public final Function getNumberOfPositionsFunction() {
			return numberOfPositionsFunction;
		}

		public final void setNumberOfPositionsFunction(
				Function numberOfPositionsFunction) {
			this.numberOfPositionsFunction = numberOfPositionsFunction;
		}

		public final Function getNumberOfUnitsFunction() {
			return numberOfUnitsFunction;
		}

		public final void setNumberOfUnitsFunction(Function numberOfUnitsFunction) {
			this.numberOfUnitsFunction = numberOfUnitsFunction;
		}

		public final Distributor<Position, Unit> getPositionUnitDistributor() {
			return positionUnitDistributor;
		}

		public final void setPositionUnitDistributor(
				Distributor<Position, Unit> positionUnitDistributor) {
			this.positionUnitDistributor = positionUnitDistributor;
		}

		public final Distributor<Person, Position> getPersonPositionDistributor() {
			return personPositionDistributor;
		}

		public final void setPersonPositionDistributor(
				Distributor<Person, Position> personPositionDistributor) {
			this.personPositionDistributor = personPositionDistributor;
		}

		public final Distributor<Role, Position> getRolePositionDistributor() {
			return rolePositionDistributor;
		}

		public final void setRolePositionDistributor(
				Distributor<Role, Position> rolePositionDistributor) {
			this.rolePositionDistributor = rolePositionDistributor;
		}

		public final Distributor<Position, Position> getPositionReportsDistributor() {
			return positionReportsDistributor;
		}

		public final void setPositionReportsDistributor(
				Distributor<Position, Position> positionReportsDistributor) {
			this.positionReportsDistributor = positionReportsDistributor;
		}

		public final Distributor<Position, Position> getPositionDelegatesDistributor() {
			return positionDelegatesDistributor;
		}

		public final void setPositionDelegatesDistributor(
				Distributor<Position, Position> positionDelegatesDistributor) {
			this.positionDelegatesDistributor = positionDelegatesDistributor;
		}
		
		
		
		
		
		
		
		
		
		
}
