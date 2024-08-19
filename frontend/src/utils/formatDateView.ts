export const formatDate = (dateString: string) => {
    let day: number, month: number, year: number;

    if (dateString.includes('-')) {
        [year, month, day] = dateString.split('-').map(part => parseInt(part, 10));
    } else if (dateString.includes('.')) {
        [day, month, year] = dateString.split('.').map(part => parseInt(part, 10));
    } else {
        console.error("Invalid date string:", dateString);
        return dateString;
    }

    const formattedDay = String(day).padStart(2, '0');
    const formattedMonth = String(month).padStart(2, '0');

    return `${formattedMonth}/${formattedDay}/${year}`;
};