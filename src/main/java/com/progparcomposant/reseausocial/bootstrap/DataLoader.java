package com.progparcomposant.reseausocial.bootstrap;

import com.progparcomposant.reseausocial.converters.FriendshipConverter;
import com.progparcomposant.reseausocial.converters.InvitationConverter;
import com.progparcomposant.reseausocial.converters.PostConverter;
import com.progparcomposant.reseausocial.converters.UserConverter;
import com.progparcomposant.reseausocial.dto.FriendshipDTO;
import com.progparcomposant.reseausocial.dto.InvitationDTO;
import com.progparcomposant.reseausocial.dto.PostDTO;
import com.progparcomposant.reseausocial.dto.UserDTO;
import com.progparcomposant.reseausocial.repositories.FriendshipRepository;
import com.progparcomposant.reseausocial.repositories.InvitationRepository;
import com.progparcomposant.reseausocial.repositories.PostRepository;
import com.progparcomposant.reseausocial.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe utilsée à lors du déploiement de l'application pour remplir la
 * base de données H2 avec un minimum de données à manipuler.
 */
@AllArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {

    @Override
    public void run(String... args) {
        loadUsers();
        loadRelations();
        loadInvitations();
        loadPosts();
    }


    private void loadUsers() {
        String[] firstName = {"Marc", "Jean", "Paul", "Sophie", "Helene"};
        String[] lastName = {"Roche", "Martinez", "Vidal", "Colin", "Aubert"};
        String[] email = {"marc.roche@ll.fr", "jean.martinez@traiteur.com", "p.vidal@to.fr", "so.co@soco.com", "hel.au@hello.com"};
        String[] phoneNumber = {"0665576964", "0200854830", "0737538615", "0132470478", "0207822185"};
        String[] city = {"Paris", "Londres", "Trifouilly-les-Oies", "New-York", "Taiwan"};
        String[] birthdate = {"1982-02-03", "2000-06-22", "1998-10-07", "1975-11-11", "1987-12-08"};
        Timestamp[] signIn = {Timestamp.valueOf("2000-03-20 12:12:12"), Timestamp.valueOf("2010-07-20 08:15:12"), Timestamp.valueOf("2002-12-30 10:05:00"), Timestamp.valueOf("2014-09-10 13:02:12"), Timestamp.valueOf("2009-07-13 05:00:12")};
        String[] username = {"marcR","jeanM","paulV","sophieC","heleneA"};
        String[] pass = {"1234", "univers", "L@pin", "m0t2p@Ss€", "UneSaisieDeMot"};
        List<UserDTO> userDTOList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            UserDTO userDTO = new UserDTO(firstName[i], lastName[i], Date.valueOf(birthdate[i]), email[i], phoneNumber[i], city[i], signIn[i],username[i], passwordEncoder.encode(pass[i]));
            userDTOList.add(userDTO);
        }
        userRepository.saveAll(userConverter.dtoToEntity(userDTOList));


    }

    private void loadRelations() {
        List<FriendshipDTO> friendshipDTOList = new ArrayList<>();
        friendshipDTOList.add(new FriendshipDTO(1L, 2L, Timestamp.valueOf("2020-11-04 12:00:12")));
        friendshipDTOList.add(new FriendshipDTO(2L, 3L, Timestamp.valueOf("2020-12-08 10:10:12")));
        friendshipDTOList.add(new FriendshipDTO(3L, 1L, Timestamp.valueOf("2020-09-28 12:12:12")));
        friendshipDTOList.add(new FriendshipDTO(4L, 2L, Timestamp.valueOf("2020-11-14 08:30:12")));
        friendshipDTOList.add(new FriendshipDTO(4L, 5L, Timestamp.valueOf("2020-10-30 06:30:12")));
        friendshipDTOList.add(new FriendshipDTO(5L, 1L, Timestamp.valueOf("2020-11-02 10:10:12")));
        friendshipRepository.saveAll(friendshipConverter.dtoToEntity(friendshipDTOList));
    }

    private void loadInvitations() {
        List<InvitationDTO> invitationDTOList = new ArrayList<>();
        invitationDTOList.add(new InvitationDTO(2L, 4L, Timestamp.valueOf("2020-12-06 02:30:00")));
        invitationDTOList.add(new InvitationDTO(5L, 4L, Timestamp.valueOf("2020-11-20 10:38:20")));
        invitationDTOList.add(new InvitationDTO(5L, 3L, Timestamp.valueOf("2020-10-16 06:40:50")));
        invitationRepository.saveAll(invitationConverter.dtoToEntity(invitationDTOList));
    }

    private void loadPosts() {
        List<PostDTO> postDTOList = new ArrayList<>();
        postDTOList.add(new PostDTO("Post privé de l'utilisateur 1", Timestamp.valueOf("2020-08-08 10:00:44"), false, 1L));
        postDTOList.add(new PostDTO("Post public de l'utilisateur 1", Timestamp.valueOf("2020-08-08 10:02:40"), true, 1L));
        postDTOList.add(new PostDTO("Post public de l'utilisateur 2", Timestamp.valueOf("2020-10-09 07:05:42"), true, 2L));
        postDTOList.add(new PostDTO("Post privé de l'utilisateur 2", Timestamp.valueOf("2020-10-10 09:15:00"), false, 2L));
        postDTOList.add(new PostDTO("Post public de l'utilisateur 3", Timestamp.valueOf("2020-08-08 10:02:43"), true, 3L));
        postDTOList.add(new PostDTO("Post public de l'utilisateur 3", Timestamp.valueOf("2020-08-08 09:27:13"), true, 3L));
        postDTOList.add(new PostDTO("Post privé de l'utilisateur 4", Timestamp.valueOf("2020-12-01 12:00:00"), false, 4L));
        postDTOList.add(new PostDTO("Post privé de l'utilisateur 4", Timestamp.valueOf("2020-11-20 11:14:09"), false, 4L));
        postRepository.saveAll(postConverter.dtoToEntity(postDTOList));
    }

    private final FriendshipRepository friendshipRepository;
    private final FriendshipConverter friendshipConverter;
    private final InvitationConverter invitationConverter;
    private final InvitationRepository invitationRepository;
    private final PostConverter postConverter;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;

}
