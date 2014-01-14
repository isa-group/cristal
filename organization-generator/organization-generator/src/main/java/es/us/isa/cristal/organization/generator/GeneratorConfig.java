package es.us.isa.cristal.organization.generator;

import es.us.isa.cristal.organization.generator.functions.Function;

public class GeneratorConfig {
	
		private Function numberOfPersonsFunction;
		
		private Function numberOfRolesFunction;
		
		private Function numberOfPositionsFunction;
		
		private Function numberOfUnitsFunction;
		
		private Function personPositionRelationFunction;
		
		private Function positionRoleRelationFunction;
		
		private Function delegateRelationFunction;
		
		private Function reportedByRelationFunction;
		
		private Function positionUnitRelationFunction;
		
		
		
		
		public GeneratorConfig(Function numberOfPersonsFunction,
				Function numberOfRolesFunction,
				Function numberOfPositionsFunction,
				Function numberOfUnitsFunction,
				Function personPositionRelationFunction,
				Function positionRoleRelationFunction,
				Function delegateRelationFunction,
				Function reportedByRelationFunction,
				Function positionUnitRelationFunction) {
			super();
			this.numberOfPersonsFunction = numberOfPersonsFunction;
			this.numberOfRolesFunction = numberOfRolesFunction;
			this.numberOfPositionsFunction = numberOfPositionsFunction;
			this.numberOfUnitsFunction = numberOfUnitsFunction;
			this.personPositionRelationFunction = personPositionRelationFunction;
			this.positionRoleRelationFunction = positionRoleRelationFunction;
			this.delegateRelationFunction = delegateRelationFunction;
			this.reportedByRelationFunction = reportedByRelationFunction;
			this.positionUnitRelationFunction = positionUnitRelationFunction;
		}

		public final Function getNumberOfPersonsFunction() {
			return numberOfPersonsFunction;
		}

		public final Function getNumberOfRolesFunction() {
			return numberOfRolesFunction;
		}

		public final Function getNumberOfPositionsFunction() {
			return numberOfPositionsFunction;
		}

		public final Function getNumberOfUnitsFunction() {
			return numberOfUnitsFunction;
		}

		public final Function getPersonPositionRelationFunction() {
			return personPositionRelationFunction;
		}

		public final Function getPositionRoleRelationFunction() {
			return positionRoleRelationFunction;
		}

		public final Function getDelegateRelationFunction() {
			return delegateRelationFunction;
		}

		public final Function getReportedByRelationFunction() {
			return reportedByRelationFunction;
		}

		public final Function getPositionUnitRelationFunction() {
			return positionUnitRelationFunction;
		}

		public final void setNumberOfPersonsFunction(Function numberOfPersonsFunction) {
			this.numberOfPersonsFunction = numberOfPersonsFunction;
		}

		public final void setNumberOfRolesFunction(Function numberOfRolesFunction) {
			this.numberOfRolesFunction = numberOfRolesFunction;
		}

		public final void setNumberOfPositionsFunction(
				Function numberOfPositionsFunction) {
			this.numberOfPositionsFunction = numberOfPositionsFunction;
		}

		public final void setNumberOfUnitsFunction(Function numberOfUnitsFunction) {
			this.numberOfUnitsFunction = numberOfUnitsFunction;
		}

		public final void setPersonPositionRelationFunction(
				Function personPositionRelationFunction) {
			this.personPositionRelationFunction = personPositionRelationFunction;
		}

		public final void setPositionRoleRelationFunction(
				Function positionRoleRelationFunction) {
			this.positionRoleRelationFunction = positionRoleRelationFunction;
		}

		public final void setDelegateRelationFunction(Function delegateRelationFunction) {
			this.delegateRelationFunction = delegateRelationFunction;
		}

		public final void setReportedByRelationFunction(
				Function reportedByRelationFunction) {
			this.reportedByRelationFunction = reportedByRelationFunction;
		}

		public final void setPositionUnitRelationFunction(
				Function positionUnitRelationFunction) {
			this.positionUnitRelationFunction = positionUnitRelationFunction;
		}
		
		
		
		
		
		
}
