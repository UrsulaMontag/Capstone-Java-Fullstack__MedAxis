export const ageCalculation = (birthDate: string, actualDate: string): number => {
    const birthDateObj = new Date(birthDate);
    const actualDateObj = new Date(actualDate);

    let age: number = actualDateObj.getFullYear() - birthDateObj.getFullYear();
    const birthdayInActualYear = new Date(actualDateObj.getFullYear(), actualDateObj.getMonth(), actualDateObj.getDate());

    if (actualDateObj < birthdayInActualYear) {
        age--;
    }
    return age;
}