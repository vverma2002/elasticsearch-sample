package com.vik.elastic.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.springframework.stereotype.Service;

import com.vik.elastic.modal.Citizenship;
import com.vik.elastic.modal.CompositeID;
import com.vik.elastic.modal.Identification;
import com.vik.elastic.modal.Name;
import com.vik.elastic.modal.PhysicalDescription;
import com.vik.elastic.modal.Request;

import net.datafaker.Faker;

@Service
public class RandomRequestGenerator {

	public static void main(String[] args) {
		List<Request> generateRequests = generateRequestList(1, 5);
		generateRequests.forEach(System.out::println);

//		List<Request> generateRequests = generateRequests(1, 10000);
//		System.out.println(generateRequests.size());

//		System.out.println(faker.regexify("[A-Z]{2}[0-9]{7}"));
//		System.out.println(faker.passport().valid());
//		System.out.println(faker.regexify("ID[a-zA-Z0-9]{8}"));
	}

	private final static Faker faker = new Faker();

	public static final long MAX_ID = 1000000000000l;
	private static final int MAX_SEQUENCE_NUMBER = 10;
	private static final int MAX_CITIZENSHIP_COUNT = 2;
	private static final int MAX_IDENTIFICATION_COUNT = 1;
	private static final int MAX_NAME_COUNT = 3;
	private static final int MAX_PHYSICAL_DESCRIPTION_COUNT = 1;

	private static final Random RANDOM = new Random();
	private static final String[] DESIGNATED_ACTION_CODE = { "Modify", "Add", "Delete" };
	private static final String[] OWNER_PRODUCER = { "USA", "UK", "JP", "FR", "DE" };
	private static final String[] CLASSIFICATION = { "U", "C" };
	private static final String[] DISSEMINATION_CONTROL = { "", "C" };
	private static final String[] BOOLEAN = { "true", "false" };
	private static final String[] DESIGNATED_ACTION_SHORT_CODE = { "M", "A", "D" }; // TODO check in prod
	private static final String[] GENDER = { "Male", "Female" };

	private static String getRandom(String[] type) {
		return type[RANDOM.nextInt(type.length)];
	}

	public static List<Request> generateRequestList(long startId, long endId) {
		return LongStream.range(startId, endId + 1).mapToObj(i -> createRequest(i)).collect(Collectors.toList());
	}

	private static Request createRequest(long requestId) {
		return Request.builder()//
				.requestId(requestId) //
				.designatedActionCode(getRandom(DESIGNATED_ACTION_CODE)) //
				.ownerProducer(getRandom(OWNER_PRODUCER)) //
				.classification(getRandom(CLASSIFICATION)) //
				.disseminationControls(getRandom(DISSEMINATION_CONTROL)) //
				.identificationId(faker.random().nextLong(MAX_ID)) //
				.personId(faker.random().nextLong(MAX_ID)) //
				.sequenceNumber(faker.random().nextLong(MAX_SEQUENCE_NUMBER)) //
				.tag(getRandom(BOOLEAN)) //
				.tsanof(getRandom(BOOLEAN)) //
				.tsasel(getRandom(BOOLEAN)) //
				.identityDesignatedActionCode(getRandom(DESIGNATED_ACTION_SHORT_CODE)) //
				.identityLastUpdated(faker.date().birthday().toInstant()) //
				.citizenshipList(generateCitizenshipList(requestId, faker.random().nextLong(MAX_CITIZENSHIP_COUNT))) //
				.identificationList(
						generateIdentificationList(requestId, faker.random().nextLong(MAX_IDENTIFICATION_COUNT))) //
				.nameList(generateNameList(requestId, faker.random().nextLong(MAX_NAME_COUNT))) //
				.physicalDescriptionList(generatePhysicalDescriptionList(requestId,
						faker.random().nextLong(MAX_PHYSICAL_DESCRIPTION_COUNT))) //

				.build();
	}

	public static List<Citizenship> generateCitizenshipList(long requestId, long count) {
		return LongStream.range(0, count + 1).mapToObj(i -> createCitizenship(requestId)).collect(Collectors.toList());
	}

	private static Citizenship createCitizenship(long requestId) {
		return Citizenship.builder()//
				.id(new CompositeID(faker.regexify("ID[A-Z0-9]{12}"), requestId)) //
				.countryCode(faker.country().countryCode2().toUpperCase()) //
				.build();
	}

	public static List<Identification> generateIdentificationList(long requestId, long count) {
		return LongStream.range(0, count + 1).mapToObj(i -> createIdentification(requestId))
				.collect(Collectors.toList());
	}

	private static Identification createIdentification(long requestId) {
		return Identification.builder()//
				.id(new CompositeID(faker.regexify("ID[A-Z0-9]{12}"), requestId)) //
				.identificationType("PassportNumber") //
				.identifier(faker.passport().valid()) //
				.countryCode(faker.country().countryCode2().toUpperCase()) //
				.fullName(faker.name().nameWithMiddle()) //
				.givenName(faker.name().fullName()) //
				.surName(faker.name().lastName()) //
				.dateOfBirth(LocalDate.ofInstant(faker.date().birthday().toInstant(), ZoneId.systemDefault())).build();
	}

	public static List<Name> generateNameList(long requestId, long count) {
		return LongStream.range(0, count + 1).mapToObj(i -> createName(requestId)).collect(Collectors.toList());
	}

	private static Name createName(long requestId) {
		return Name.builder()//
				.id(new CompositeID(faker.regexify("ID[A-Z0-9]{12}"), requestId)) //
				.fullName(faker.name().nameWithMiddle()) //
				.givenName(faker.name().fullName()) //
				.surName(faker.name().lastName()) //
				.build();
	}

	public static List<PhysicalDescription> generatePhysicalDescriptionList(long requestId, long count) {
		return LongStream.range(0, count + 1).mapToObj(i -> createPhysicalDescription(requestId))
				.collect(Collectors.toList());
	}

	private static PhysicalDescription createPhysicalDescription(long requestId) {
		return PhysicalDescription.builder()//
				.id(new CompositeID(faker.regexify("ID[A-Z0-9]{12}"), requestId)) //
				.dateOfBirth(LocalDate.ofInstant(faker.date().birthday().toInstant(), ZoneId.systemDefault()))
				.gender(getRandom(GENDER)).build();
	}

	//
//	//// @formatter:off
//	private static final Random RANDOM = new Random();
//	private static final String[] MEDIA_NAMES = {"name1", "name2", "name3", "name4", "name5"};
//	private static final String[] COUNTRY_CODES = {"USA", "UK", "CN", "JP", "FR", "DE"};
//	private static final String[] FULL_NAMES = {"John Doe", "Jane Doe", "Bob Smith", "Alice Lee", "David Kim"};
//	private static final String[] GIVEN_NAMES = {"John", "Jane", "Bob", "Alice", "David"};
//	private static final String[] SURNAMES = {"Doe", "Smith", "Lee", "Kim", "Johnson", "Jones"};
//	private static final int[] DOB_YEAR_RANGE = {1980, 1981, 1982, 1983, 1984, 1985, 1986, 1987, 1988, 1989, 1990, 1991};
//	private static final int[] DOB_MONTH_RANGE = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
//	private static final int[] DOB_DAY_RANGE = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31};
//	private static final String[] GENDERS = {"Male", "Female"};
//
//	private static String randomMediaName() {
//		return MEDIA_NAMES[RANDOM.nextInt(MEDIA_NAMES.length)];
//	}
//	private static String randomCountryCode() {
//		return COUNTRY_CODES[RANDOM.nextInt(COUNTRY_CODES.length)];
//	}
//	private static String randomFullName() {
//		return FULL_NAMES[RANDOM.nextInt(FULL_NAMES.length)];
//	}
//	private static String randomGivenName() {
//		return GIVEN_NAMES[RANDOM.nextInt(GIVEN_NAMES.length)];
//	}
//	private static String randomSurname() {
//		return SURNAMES[RANDOM.nextInt(SURNAMES.length)];
//	}
//	private static int randomDobYear() {
//		return DOB_YEAR_RANGE[RANDOM.nextInt(DOB_YEAR_RANGE.length)];
//	}
//	private static int randomDobMonth() {
//		return DOB_MONTH_RANGE[RANDOM.nextInt(DOB_MONTH_RANGE.length)];
//	}
//	private static int randomDobDay() {
//		return DOB_DAY_RANGE[RANDOM.nextInt(DOB_DAY_RANGE.length)];
//	}
//	private static String randomGender() {
//		return GENDERS[RANDOM.nextInt(GENDERS.length)];
//	}
//	// @formatter:on	

//	String mediaName1 = randomMediaName();
//	String mediaName2 = randomMediaName();
//	boolean mediaPosted1 = RANDOM.nextBoolean();
//	boolean mediaPosted2 = RANDOM.nextBoolean();
//	String country1 = randomCountryCode();
//	String country2 = randomCountryCode();
//	String passportNum = "P" + String.format("%04d", RANDOM.nextInt(10000));
//	String issueCountry = randomCountryCode();
//	String fullName = randomFullName();
//	String givenName = randomGivenName();
//	String surname = randomSurname();
//	String fullName2 = randomFullName();
//	String givenName2 = randomGivenName();
//	String surname2 = randomSurname();
//	int dob = randomDobDay();
//	int year = randomDobYear();
//	int dobMonth = randomDobMonth();
//	int dobDay = randomDobDay();
//	int dobYear2 = randomDobYear();
//	int dobMonth2 = randomDobMonth();
//	int dobDay2 = randomDobDay();
//	String gender = randomGender();

//		private void printRequest(List<Request> requestList) {
//			// Print the extracted information
//			for (Request request : requestList) {
//				System.out.println("RequestID: " + request.getRequestId());
	//
//				MediaDesignation media = request.getMediaDesignation();
//				System.out.println("DesignatedActionCode: " + media.getDesignatedActionCode());
//				System.out.println("OwnerProducer: " + media.getOwnerProducer());
//				System.out.println("Classification: " + media.getClassification());
//				System.out.println("DisseminationControls: " + media.getDisseminationControls());
//				System.out.println("IdentificationID: " + media.getIdentificationList().toString());
//				System.out.println("PersonID: " + media.getPersonID());
//				System.out.println("SequenceNumber: " + media.getSequenceNumber());
	//
//				for (MediaDisposition disposition : media.getMediaDisposition()) {
//					System.out.println("MediaDisposition: ");
//					System.out.println("\tMediaName: " + disposition.getMediaName());
//					System.out.println("\tMediaPostedIndicator: " + disposition.getMediaPostedIndicator());
//				}
//				Identity identity = request.getIdentity();
//				System.out.println("Identity DesignatedActionCode: " + identity.getDesignatedActionCode());
//				System.out.println("Identity LastUpdated: " + identity.getLastUpdated());
	//
//				CitizenshipList citizenshipList = identity.getCitizenshipList();
//				for (Citizenship citizenship : citizenshipList.getCitizenshipList()) {
//					System.out.println("Citizenship ID: " + citizenship.getId());
//					System.out.println("Citizenship Country: " + citizenship.getCountry());
//				}
	//
//				IdentificationList identificationList = identity.getIdentificationList();
//				for (Identification identification : identificationList.getIdentificationList()) {
//					System.out.println("Identification ID: " + identification.getId());
//					System.out.println("IdentifierType: " + identification.getIdentifierType());
//					System.out.println("Identifier: " + identification.getIdentifier());
//					System.out.println("IssueCountry: " + identification.getIssueCountry());
	//
//					Person person = identification.getPerson();
//					if (person != null) {
//						System.out.println("Person: ");
//						Name name = person.getName();
//						System.out.println("\tFullName: " + name.getFullName());
//						System.out.println("\tGivenName: " + name.getGivenName());
//						System.out.println("\tSurname: " + name.getSurname());
	//
//						DateOfBirth dob = person.getDateOfBirth();
//						if (dob != null) {
//							System.out.println("\tDateOfBirth: ");
//							System.out.println("\t\tYear: " + dob.getYear());
//							System.out.println("\t\tMonth: " + dob.getMonth());
//							System.out.println("\t\tDayOfMonth: " + dob.getDayOfMonth());
//						}
//					}
//				}
	//
//				NameList nameList = identity.getNameList();
//				for (Name name : nameList.getNameList()) {
//					System.out.println("Name ID: " + name.getId());
//					System.out.println("FullName: " + name.getFullName());
//					System.out.println("GivenName: " + name.getGivenName());
//					System.out.println("Surname: " + name.getSurname());
//				}
	//
//				PhysicalDescriptionList physicalDescList = identity.getPhysicalDescriptionList();
//				for (PhysicalDescription physicalDesc : physicalDescList.getPhysicalDescriptionList()) {
//					System.out.println("PhysicalDescription ID: " + physicalDesc.getId());
//					DateOfBirth dob = physicalDesc.getDateOfBirth();
//					if (dob != null) {
//						System.out.println("\tDateOfBirth: ");
//						System.out.println("\t\tYear: " + dob.getYear());
//						System.out.println("\t\tMonth: " + dob.getMonth());
//						System.out.println("\t\tDayOfMonth: " + dob.getDayOfMonth());
//					}
//					System.out.println("Gender: " + physicalDesc.getGender());
//				}
	//
//			}
//		}

}
