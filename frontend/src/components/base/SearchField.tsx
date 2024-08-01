import useGlobalStore from "../../stores/useGloblaStore.ts";
import {ChangeEvent} from "react";
import {StyledSearchField} from "../../styles/SearchField.styled.ts";

export default function SearchField() {
    const setSearchTerm = useGlobalStore((state) => state.setSearchTerm);

    const handleSearchTermChange = (event: ChangeEvent<HTMLInputElement>) => {
        setSearchTerm(event.target.value);
    }

    return (
        <StyledSearchField type="text"
                           placeholder="Search..."
                           alt="enter-search-input"
                           title="Enter search input"
                           onChange={handleSearchTermChange}/>
    )
}