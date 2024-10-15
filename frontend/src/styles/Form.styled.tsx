import styled from "styled-components";

const StyledForm = styled.form`
    display: flex;
    flex-direction: column;
    width: 100%;
    gap: .3rem;
    margin: 0;
    padding: 0;
`;

const FormGroup = styled.div`
    display: flex;
    align-items: baseline;
    margin-bottom: 1.8rem;
    gap: .3rem;
`;

const FormLabel = styled.label`
    flex: 1; 
    margin-right: 10px; 
    font-size: 1.2rem;
`;

const FormInput = styled.input`
    flex: 2; 
    padding: 8px; 
    border: 1px solid #ccc; 
    border-radius: 4px; 
    width: 100%;
`;

const FormTextarea = styled.textarea`
    flex: 2;
    padding: 6px;
    border: 1px solid #ccc;
    border-radius: 4px;
    resize: vertical;
`;

const FormSelect = styled.select`
    flex: 2; 
    padding: 8px;
    border: 1px solid #ccc; 
    border-radius: 4px;
    width: 100%;
`;

const FormHeading = styled.h4`
    width: 100%; 
    font-size: 1.2rem; 
    margin: 20px 0; 
    text-align: center; 
    border-bottom: 2px solid #ccc; 
    padding-bottom: 10px;
`;

const ErrorInfo = styled.span`
    color: red;
    font-size: 0.9rem;
    margin-top: 4px;
`;

export { StyledForm, FormGroup, FormLabel, FormInput, FormTextarea, FormSelect, FormHeading, ErrorInfo };